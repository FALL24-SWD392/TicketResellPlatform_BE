package com.swd392.ticket_resell_be.dtos.requests;

import jakarta.validation.constraints.NotEmpty;

public record GetAccessTokenRequest(@NotEmpty(message = "TOKEN_EMPTY") String token) {
}
