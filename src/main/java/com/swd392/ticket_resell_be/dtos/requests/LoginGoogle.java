package com.swd392.ticket_resell_be.dtos.requests;

import jakarta.validation.constraints.NotEmpty;

public record LoginGoogle(
//        @NotEmpty(message = "GOOGLE_TOKEN_EMPTY") String googleToken,
        String email,
        String username,
        String avatar
) {
}
