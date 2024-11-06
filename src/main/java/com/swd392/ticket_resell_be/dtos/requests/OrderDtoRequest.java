package com.swd392.ticket_resell_be.dtos.requests;

import com.swd392.ticket_resell_be.enums.Categorize;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record OrderDtoRequest(
        @NotNull(message = "CHAT_BOX_ID_MUST_NOT_BE_NULL")
        String chatBoxId,

        @NotNull(message = "STATUS_MUST_NOT_BE_NULL")
        Categorize status
) implements Serializable {
}
