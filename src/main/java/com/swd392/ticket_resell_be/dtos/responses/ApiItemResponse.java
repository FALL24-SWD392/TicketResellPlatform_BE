package com.swd392.ticket_resell_be.dtos.responses;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ApiItemResponse<T>(
        T data,
        HttpStatus status,
        String message
) {
}
