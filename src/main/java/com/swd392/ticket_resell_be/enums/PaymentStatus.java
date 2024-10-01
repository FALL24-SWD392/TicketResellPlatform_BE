package com.swd392.ticket_resell_be.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    SUCCESS(0),  // Assuming 0 is the success code from VNPay
    FAILURE(1);  // Assuming 1 is the failure code from VNPay

    private final int code;

    PaymentStatus(int code) {
        this.code = code;
    }

    public static PaymentStatus fromCode(int code) {
        for (PaymentStatus status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid payment status code: " + code);
    }
}
