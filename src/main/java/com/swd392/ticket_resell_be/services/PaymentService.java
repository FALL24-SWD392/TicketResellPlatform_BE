package com.swd392.ticket_resell_be.services;

import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;

public interface PaymentService {
    ApiItemResponse<String> handlePaymentCallback(
            String vnpAmount, String vnpBankCode, String vnpBankTranNo, String vnpCardType,
            String orderInfo, String vnpPayDate, String responseCode, String vnpTmnCode,
            String vnpTransactionNo, String transactionStatus, String vnpTxnRef, String vnpSecureHash
    );
}
