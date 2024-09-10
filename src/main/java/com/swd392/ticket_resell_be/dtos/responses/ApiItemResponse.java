package com.swd392.ticket_resell_be.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiItemResponse<T>(T data, HttpStatus status, String message) {
}
