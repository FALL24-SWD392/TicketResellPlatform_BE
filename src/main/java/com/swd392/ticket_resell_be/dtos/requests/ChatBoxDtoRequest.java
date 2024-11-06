package com.swd392.ticket_resell_be.dtos.requests;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

public record ChatBoxDtoRequest(
        @NotNull(message = "CHAT_BOX_ID_MUST_NOT_BE_NULL")
        String id,

        @NotNull(message = "CHAT_ID_MUST_NOT_BE_NULL")
        String chat_id,

        @NotNull(message = "SENDER_ID_MUST_NOT_BE_NULL")
        UUID sender_id,

        @NotNull(message = "RECIPIENT_ID_MUST_NOT_BE_NULL")
        UUID recipient_id,

        @NotNull(message = "TICKET_ID_MUST_NOT_BE_NULL")
        UUID ticket_id
) implements Serializable {
}

