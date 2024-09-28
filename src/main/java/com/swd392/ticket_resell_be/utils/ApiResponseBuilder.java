package com.swd392.ticket_resell_be.utils;

import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiResponseBuilder {

    public <T> ApiItemResponse<T> buildResponse(T data, HttpStatus status, String message) {
        return new ApiItemResponse<>(data, status, message);
    }

    public <T> ApiItemResponse<T> buildResponse(T data, HttpStatus status) {
        return new ApiItemResponse<>(data, status, null);
    }

    public <T> ApiItemResponse<T> buildResponse(HttpStatus status, String message) {
        return new ApiItemResponse<>(null, status, message);
    }

    public <T> ApiListResponse<T> buildResponse(List<T> data, int size, int page, long totalSize,
                                                int totalPage, HttpStatus status, String message) {
        return new ApiListResponse<>(data, size, page, totalSize, totalPage, status, message);
    }

}
