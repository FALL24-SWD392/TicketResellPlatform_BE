package com.swd392.ticket_resell_be.dtos.requests;

import jakarta.validation.constraints.NotEmpty;

public record LogoutRequest(@NotEmpty(message = "INVALID_TOKEN") String token) {
}
