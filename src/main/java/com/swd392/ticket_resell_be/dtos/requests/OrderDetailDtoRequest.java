package com.swd392.ticket_resell_be.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.util.UUID;

public record OrderDetailDtoRequest(
        @NotNull(message = "ORDER_ID_MUST_NOT_BE_NULL")
        UUID orderId,

        @NotBlank(message = "TICKET_TITLE_EMPTY")
        String ticketTitle,

        @NotNull(message = "QUANTITY_MUST_NOT_BE_NULL")
        @Positive(message = "Quantity must be greater than 0")
        int quantity
) implements Serializable {
}
