package com.swd392.ticket_resell_be.dtos.requests;

import com.swd392.ticket_resell_be.enums.Categorize;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.UUID;

public record ReportDtoRequest(
        @NotEmpty(message = "REPORTED_BY_ID_EMPTY")
        UUID reporter_id,

        @NotEmpty(message = "REPORTED_ID_EMPTY")
        UUID reported_id,

        @NotEmpty(message = "ORDER_ID_EMPTY")
        UUID order_id,
        @NotEmpty(message = "STATUS_EMPTY")
        Categorize status,

        @NotEmpty(message = "REPORT_DESCRIPTION_EMPTY")
        String description,

        @NotEmpty(message = "CREATED_BY_EMPTY")
        String created_by,

        @NotEmpty(message = "DESCRIPTION_EMPTY")
        String created_at,

        @NotEmpty(message = "DESCRIPTION_EMPTY")
        String updated_by,

        @NotEmpty(message = "DESCRIPTION_EMPTY")
        String updated_at
) implements Serializable {
}
