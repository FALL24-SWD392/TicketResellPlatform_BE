package com.swd392.ticket_resell_be.dtos.requests;

public record PageDtoRequest(
        int size,
        int page
) {
}
