package com.swd392.ticket_resell_be.dtos.requests;

import jakarta.validation.constraints.NotEmpty;

public record ForgotPasswordRequest(@NotEmpty(message = "EMAIL_INVALID") String email) {
}
