package com.swd392.ticket_resell_be.utils;

import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ApiResponseBuilder {

    public <T> ApiItemResponse<T> buildResponse(T data, HttpStatus status, String message) {
        return new ApiItemResponse<>(data, status, message);
    }

}
