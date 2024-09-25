package com.swd392.ticket_resell_be.controllers;

import com.swd392.ticket_resell_be.dtos.requests.SubscriptionDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        return ResponseEntity.ok(response); // Return OK for the creation response
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ApiItemResponse<Subscription>>> getSubscriptionById(@PathVariable("id") UUID packageId) {
        Optional<ApiItemResponse<Subscription>> response = subscriptionService.getSubscriptionById(packageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiItemResponse<List<Subscription>>> getAllSubscriptions() {
        ApiItemResponse<List<Subscription>> response = subscriptionService.getAllSubscriptions();
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

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiItemResponse<Void>> deleteSubscription(@PathVariable("id") UUID packageId) {
        ApiItemResponse<Void> response = subscriptionService.deleteSubscription(packageId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/submitOrder")
    public String submitOrder(@RequestParam("packageId") UUID packageId, HttpServletRequest request) {
        // Retrieve the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Subscription pkg = subscriptionService.getSubscriptionById(packageId)
                .orElseThrow(() -> new AppException(ErrorCode.PACKAGE_NOT_FOUND)).data(); // Ensure the package exists
        int orderTotal = pkg.getPrice();
        String orderInfo = "Thanh toán cho gói " + pkg.getName(); // Example order description
        VNPayOrderResponse orderResponse = vnPayService.createOrder(orderTotal, orderInfo);
        transactionService.savePendingTransaction(pkg, user, orderResponse.orderCode());
                return "redirect:" + orderResponse.vnpayUrl();
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
                Optional<Transaction> transactionOpt = transactionService.findTransactionByDescription(vnpTxnRef);
                if (transactionOpt.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaction not found");
                }

                Transaction transaction = transactionOpt.get();
                transaction.setStatus(TransactionStatus.COMPLETED);
                transactionService.updateTransactionStatus(transaction.getId(), TransactionStatus.COMPLETED);
                membershipService.create(transaction.getUser(), transaction.getSubscription());

                // Success response
                return ResponseEntity.ok("Thanh toán thành công!");
            } else {
                // Payment failed
                return ResponseEntity.badRequest().body("Thanh toán thất bại. Mã lỗi: " + responseCode);
            }
        } catch (AppException e) {
            // Handle known exceptions
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi xử lý thanh toán. Vui lòng thử lại!");
        }
    }

}





