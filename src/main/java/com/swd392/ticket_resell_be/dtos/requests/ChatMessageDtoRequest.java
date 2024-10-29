package com.swd392.ticket_resell_be.dtos.requests;

import com.swd392.ticket_resell_be.entities.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

public record ChatMessageDtoRequest(
        @NotNull(message = "CHAT_BOX_ID_MUST_NOT_BE_NULL")
        UUID chat_box_id,

        @NotNull(message = "SENDER_ID_MUST_NOT_BE_NULL")
        User sender_id,

        @NotEmpty(message = "MESSAGE_EMPTY")
        String message
) implements Serializable {
}
