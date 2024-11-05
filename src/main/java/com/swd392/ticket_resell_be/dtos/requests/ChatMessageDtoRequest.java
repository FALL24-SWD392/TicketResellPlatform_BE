package com.swd392.ticket_resell_be.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

public record ChatMessageDtoRequest(
        @NotNull(message = "ID_CAN_NOT_NULL")
        String id,

        @NotNull(message = "CHAT_ID_CAN_NOT_NULL")
        String chatId,

        @NotNull(message = "SENDER_ID_MUST_NOT_BE_NULL")
        UUID sender_id,

        @NotNull(message = "RECIPIENT_ID_MUST_NOT_BE_NULL")
        UUID recipient_id,

        @NotEmpty(message = "MESSAGE_EMPTY")
        String message
) implements Serializable {
}
