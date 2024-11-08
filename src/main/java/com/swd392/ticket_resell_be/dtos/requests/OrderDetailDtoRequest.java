package com.swd392.ticket_resell_be.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;
import java.util.UUID;

public record OrderDetailDtoRequest(
        @NotNull(message = "ORDER_ID_MUST_NOT_BE_NULL")
        UUID orderId,

        @NotNull(message = "TICKET_ID_EMPTY")
        UUID ticketId,

        @NotNull(message = "QUANTITY_MUST_NOT_BE_NULL")
        @Positive(message = "QUANTITY_MUST_BE_GREATER_THAN_0")
        int quantity
) implements Serializable {
}
