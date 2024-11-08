package com.swd392.ticket_resell_be.dtos.requests;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

public record OrderDtoRequest(
        @NotNull(message = "CHAT_BOX_ID_MUST_NOT_BE_NULL")
        String chatBoxId,

        @NotNull(message = "SENDER_EMPTY")
        UUID senderId,

        @NotNull(message = "RECIPIENT_EMPTY")
        UUID recipientId,

        @NotNull(message = "TICKET_EMPTY")
        UUID ticketId

) implements Serializable {
}
