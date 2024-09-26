package com.swd392.ticket_resell_be.dtos.requests;

import jakarta.validation.constraints.NotEmpty;

public record RegisterGoogleDtoRequest(
        @NotEmpty(message = "USERNAME_EMPTY")
        String username,
        @NotEmpty
        String token
) {
}
