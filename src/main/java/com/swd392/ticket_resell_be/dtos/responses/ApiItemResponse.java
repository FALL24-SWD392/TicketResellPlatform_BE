package com.swd392.ticket_resell_be.dtos.responses;

import org.springframework.http.HttpStatus;

public record ApiItemResponse<T>(
        T data,
        HttpStatus status,
        String message
) {
}
