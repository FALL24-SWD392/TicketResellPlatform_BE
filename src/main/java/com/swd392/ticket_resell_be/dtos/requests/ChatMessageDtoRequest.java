package com.swd392.ticket_resell_be.dtos.requests;

import com.swd392.ticket_resell_be.entities.User;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public record ChatMessageDtoRequest(
        @NotEmpty(message = "CHAT_BOX_ID_EMPTY")
        UUID chat_box_id,

        @NotEmpty(message = "SENDER_ID_EMPTY")
        User sender_id,

        @NotEmpty(message = "MESSAGE_EMPTY")
        String message,

        @NotEmpty(message = "CREATE_AT_EMPTY")
        Date created_at
) implements Serializable {
}
