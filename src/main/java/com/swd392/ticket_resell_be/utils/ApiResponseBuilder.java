package com.swd392.ticket_resell_be.utils;

import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiResponseBuilder {

    public static <T> ApiItemResponse<T> buildResponse(String message, HttpStatus status) {
        return ApiItemResponse.<T>builder()
                .message(message)
                .status(status)
                .build();
    }

    public static <T> ApiItemResponse<T> buildResponse(T data, HttpStatus status) {
        return ApiItemResponse.<T>builder()
                .data(data)
                .status(status)
                .build();
    }

    public static <T> ApiItemResponse<T> buildResponse(T data, String message, HttpStatus status) {
        return ApiItemResponse.<T>builder()
                .data(data)
                .message(message)
                .status(status)
                .build();
    }

}
