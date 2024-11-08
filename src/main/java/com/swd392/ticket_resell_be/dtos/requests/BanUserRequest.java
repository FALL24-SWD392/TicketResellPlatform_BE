package com.swd392.ticket_resell_be.dtos.requests;

import jakarta.validation.constraints.NotNull;

public record BanUserRequest(
        @NotNull(message = "USERNAME_EMPTY")
        String username
) {
}
