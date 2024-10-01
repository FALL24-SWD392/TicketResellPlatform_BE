package com.swd392.ticket_resell_be.dtos.responses;

public record LoginDtoResponse(
        String accessToken,
        String refreshToken
) {
}
