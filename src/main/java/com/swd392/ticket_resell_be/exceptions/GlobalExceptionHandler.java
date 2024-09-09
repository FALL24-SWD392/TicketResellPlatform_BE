package com.swd392.ticket_resell_be.exceptions;

import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiItemResponse<?>> handleAppException(AppException e) {
        return ResponseEntity.ok(ApiResponseBuilder
                .buildResponse(e.getErrorCode().getMessage(), e.getErrorCode().getStatus()));
    }
}
