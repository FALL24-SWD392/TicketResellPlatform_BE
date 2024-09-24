package com.swd392.ticket_resell_be.dtos.responses;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ApiListResponse<T>(
        List<T> data,
        int size,
        int page,
        long totalSize,
        int totalPage,
        HttpStatus status,
        String message
) {
}
