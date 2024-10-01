package com.swd392.ticket_resell_be.dtos.requests;

import com.swd392.ticket_resell_be.enums.Categorize;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public record FeedbackDtoRequest(
        @NotEmpty(message = "BUYER_ID_EMPTY")
        UUID buyer_id,

        @NotEmpty(message = "ORDER_ID_EMPTY")
        UUID order_id,

        @NotEmpty(message = "FEEDBACK_DESCRIPTION_EMPTY")
        String feedback_description,

        @NotEmpty(message = "STATUS_EMPTY")
        Categorize status,

        @NotEmpty(message = "CREATED_AT_EMPTY")
        Date created_at,

        @NotEmpty(message = "UPDATED_AT_EMPTY")
        Date updated_at
) implements Serializable {
}
