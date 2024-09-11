package com.swd392.ticket_resell_be.dtos.responses;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.util.List;

@Builder
public record ApiListResponse<T>(
        List<T> data,
        int size,
        int page,
        int totalSize,
        int totalPage,
        String message,
        HttpStatus status
) {
}
