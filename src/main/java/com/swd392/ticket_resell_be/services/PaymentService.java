package com.swd392.ticket_resell_be.services;

import jakarta.servlet.http.HttpServletResponse;


public interface PaymentService {
    void handlePaymentCallback(
            String vnpAmount, String vnpBankCode, String vnpBankTranNo, String vnpCardType,
            String orderInfo, String vnpPayDate, String responseCode, String vnpTmnCode,
            String vnpTransactionNo, String transactionStatus, String vnpTxnRef, String vnpSecureHash,
            HttpServletResponse response
    );
}
