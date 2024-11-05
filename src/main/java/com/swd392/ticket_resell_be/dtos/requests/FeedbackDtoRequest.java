package com.swd392.ticket_resell_be.dtos.requests;

import com.swd392.ticket_resell_be.enums.Categorize;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

public record FeedbackDtoRequest(
        @NotNull(message = "BUYER_ID_MUST_NOT_BE_NULL")
        UUID buyer_id,

        @NotNull(message = "ORDER_ID_MUST_NOT_BE_NULL")
        UUID order_id,

        @NotEmpty(message = "FEEDBACK_DESCRIPTION_EMPTY")
        String description,

        @NotNull(message = "RATING_MUST_NOT_BE_NULL")
        int rating,

        @NotNull(message = "STATUS_MUST_NOT_BE_NULL")
        Categorize status
) implements Serializable {
}
