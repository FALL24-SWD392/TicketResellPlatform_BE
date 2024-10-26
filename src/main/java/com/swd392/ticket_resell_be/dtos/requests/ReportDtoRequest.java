package com.swd392.ticket_resell_be.dtos.requests;

import com.swd392.ticket_resell_be.enums.Categorize;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

public record ReportDtoRequest(
        @NotNull(message = "REPORTED_BY_ID_MUST_NOT_BE_NULL")
        UUID reporter_id,

        @NotNull(message = "REPORTED_ID_MUST_NOT_BE_NULL")
        UUID reported_id,

        @NotNull(message = "ORDER_ID_MUST_NOT_BE_NULL")
        UUID order_id,
        @NotNull(message = "STATUS_MUST_NOT_BE_NULL")
        Categorize status,

        @NotEmpty(message = "REPORT_DESCRIPTION_MUST_NOT_BE_NULL")
        String description
) implements Serializable {
}
