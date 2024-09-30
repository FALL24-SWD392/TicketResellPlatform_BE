package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.entities.Transaction;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.services.MembershipService;
import com.swd392.ticket_resell_be.services.PaymentService;
import com.swd392.ticket_resell_be.services.TransactionService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PaymentServiceImplement implements PaymentService {

    TransactionService transactionService;
    MembershipService membershipService;
    ApiResponseBuilder apiResponseBuilder;

    @Override
    public ApiItemResponse<String> handlePaymentCallback(
            String vnpAmount, String vnpBankCode, String vnpBankTranNo, String vnpCardType,
            String orderInfo, String vnpPayDate, String responseCode, String vnpTmnCode,
            String vnpTransactionNo, String transactionStatus, String vnpTxnRef, String vnpSecureHash
    )
    {
        try {
            if ("00".equalsIgnoreCase(responseCode) && "00".equalsIgnoreCase(transactionStatus)) {
                ApiItemResponse<Transaction> transactionResponse = transactionService.findTransactionByOrderId(vnpTxnRef);
                if (transactionResponse.data() == null) {
                    return apiResponseBuilder.buildResponse(null, HttpStatus.NOT_FOUND, "Transaction not found");
                }
                Transaction transaction = transactionResponse.data();
                transaction.setStatus(Categorize.COMPLETED);
                transactionService.updateTransactionStatus(transaction.getId(), Categorize.COMPLETED);
                membershipService.updateMembership(transaction.getSeller(), transaction.getSubscription());
                return apiResponseBuilder.buildResponse("Thanh toán thành công!", HttpStatus.OK, "Payment successful");
            } else {
                return apiResponseBuilder.buildResponse(null, HttpStatus.BAD_REQUEST, "Thanh toán thất bại. Mã lỗi: " + responseCode);
            }
        } catch (AppException e) {
            return apiResponseBuilder.buildResponse(null, HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return apiResponseBuilder.buildResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi khi xử lý thanh toán. Vui lòng thử lại!");
        }
    }
}
