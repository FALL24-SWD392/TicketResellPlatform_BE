package com.swd392.ticket_resell_be.utils;

import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ApiResponseBuilder {

    public <T> ApiItemResponse<T> buildResponse(HttpStatus status, String message) {
        return ApiItemResponse.<T>builder()
                .message(message)
                .status(status)
                .build();
    }

    public <T> ApiItemResponse<T> buildResponse(T data, HttpStatus status) {
        return ApiItemResponse.<T>builder()
                .data(data)
                .status(status)
                .build();
    }

    public <T> ApiItemResponse<T> buildResponse(T data, HttpStatus status, String message) {
        return ApiItemResponse.<T>builder()
                .data(data)
                .message(message)
                .status(status)
                .build();
    }

}
