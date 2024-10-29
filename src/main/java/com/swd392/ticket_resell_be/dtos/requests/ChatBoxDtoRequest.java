package com.swd392.ticket_resell_be.dtos.requests;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

public record ChatBoxDtoRequest(
        @NotNull(message = "SELLER_ID_MUST_NOT_BE_NULL")
        UUID seller_id,

        @NotNull(message = "BUYER_ID_MUST_NOT_BE_NULL")
        UUID buyer_id,

        @NotNull(message = "TICKET_ID_MUST_NOT_BE_NULL")
        UUID ticket_id
) implements Serializable {
}

