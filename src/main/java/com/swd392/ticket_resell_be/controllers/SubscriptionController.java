package com.swd392.ticket_resell_be.controllers;

import com.swd392.ticket_resell_be.dtos.requests.SubscriptionDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.VNPayOrderResponse;
import com.swd392.ticket_resell_be.entities.Subscription;
import com.swd392.ticket_resell_be.entities.Transaction;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.enums.TransactionStatus;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.services.SubscriptionService;
import com.swd392.ticket_resell_be.services.MembershipService;
import com.swd392.ticket_resell_be.services.TransactionService;
import com.swd392.ticket_resell_be.services.UserService;
import com.swd392.ticket_resell_be.services.impls.VNPayServiceImplement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/subscriptions")
public class SubscriptionController {
     SubscriptionService subscriptionService;
     VNPayServiceImplement vnPayService;
     MembershipService membershipService;
     TransactionService transactionService;
     UserService userService;

    @PostMapping
    public ResponseEntity<ApiItemResponse<Subscription>> createSubscription(@RequestBody @Valid SubscriptionDtoRequest subscriptionDtoRequest) {
        ApiItemResponse<Subscription> response = subscriptionService.createSubscription(subscriptionDtoRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ApiItemResponse<Subscription>>> getSubscriptionById(@PathVariable("id") UUID packageId) {
        Optional<ApiItemResponse<Subscription>> response = subscriptionService.getSubscriptionById(packageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiListResponse<Subscription>> getAllSubscriptions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ApiListResponse<Subscription> response = subscriptionService.getAllSubscriptions(page, size);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiItemResponse<Subscription>> updateSubscription(
            @PathVariable("id") UUID packageId,
            @RequestBody @Valid SubscriptionDtoRequest subscriptionDtoRequest) {
        try {
            ApiItemResponse<Subscription> response = subscriptionService.updateSubscription(packageId, subscriptionDtoRequest);
            return ResponseEntity.ok(response);
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiItemResponse<>(null, HttpStatus.NOT_FOUND, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiItemResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage()));
        }
    }

    @PostMapping("/purchase-subscription")
    public String submitOrder(@RequestParam("subscriptionId") UUID subscriptionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Subscription subscription = subscriptionService.getSubscriptionById(subscriptionId)
                .orElseThrow(() -> new AppException(ErrorCode.SUBSCRIPTION_NOT_FOUND)).data();
        if (user.getReputation() < subscription.getPointRequired()) {
            throw new AppException(ErrorCode.INSUFFICIENT_REPUTATION);
        }
        int orderTotal = subscription.getPrice();
        String orderInfo = "Thanh toán cho gói " + subscription.getName();
        VNPayOrderResponse orderResponse = vnPayService.createOrder(orderTotal, orderInfo);
        transactionService.savePendingTransaction(subscription, user, orderResponse.orderCode());
        return orderResponse.vnpayUrl();
    }


    @GetMapping("/payment-callback")
    public ResponseEntity<String> paymentCallback(
            @RequestParam("vnp_Amount") String vnpAmount,
            @RequestParam("vnp_BankCode") String vnpBankCode,
            @RequestParam("vnp_BankTranNo") String vnpBankTranNo,
            @RequestParam("vnp_CardType") String vnpCardType,
            @RequestParam("vnp_OrderInfo") String orderInfo,
            @RequestParam("vnp_PayDate") String vnpPayDate,
            @RequestParam("vnp_ResponseCode") String responseCode,
            @RequestParam("vnp_TmnCode") String vnpTmnCode,
            @RequestParam("vnp_TransactionNo") String vnpTransactionNo,
            @RequestParam("vnp_TransactionStatus") String transactionStatus,
            @RequestParam("vnp_TxnRef") String vnpTxnRef,
            @RequestParam("vnp_SecureHash") String vnpSecureHash) {
        try {
            if ("00".equalsIgnoreCase(responseCode) && "00".equalsIgnoreCase(transactionStatus)) {
                ApiItemResponse<Transaction> transactionResponse = transactionService.findTransactionByOrderId(vnpTxnRef);
                if (transactionResponse.data() == null) { // Assuming data() returns null if no transaction found
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaction not found");
                }
                Transaction transaction = transactionResponse.data();
                transaction.setStatus(TransactionStatus.COMPLETED);
                transactionService.updateTransactionStatus(transaction.getId(), TransactionStatus.COMPLETED);
                membershipService.updateMembership(transaction.getSeller(), transaction.getSubscription());
                return ResponseEntity.ok("Thanh toán thành công!");
            } else {
                return ResponseEntity.badRequest().body("Thanh toán thất bại. Mã lỗi: " + responseCode);
            }
        } catch (AppException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi xử lý thanh toán. Vui lòng thử lại!");
        }
    }

}





