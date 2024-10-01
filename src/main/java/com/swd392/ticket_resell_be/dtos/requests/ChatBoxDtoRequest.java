package com.swd392.ticket_resell_be.dtos.requests;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public record ChatBoxDtoRequest(
        @NotEmpty(message = "SELLER_ID_EMPTY")
        UUID seller_id,

        @NotEmpty(message = "BUYER_ID_EMPTY")
        UUID buyer_id,

        @NotEmpty(message = "TICKET_ID_EMPTY")
        UUID ticket_id,

        @NotEmpty(message = "CREATE_AT_EMPTY")
        Date created_at
) implements Serializable {
}

