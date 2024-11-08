package com.swd392.ticket_resell_be.dtos.requests;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record ChatBoxDtoRequest(
        @NotNull(message = "CHAT_BOX_ID_MUST_NOT_BE_NULL")
        String id
) implements Serializable {
}

