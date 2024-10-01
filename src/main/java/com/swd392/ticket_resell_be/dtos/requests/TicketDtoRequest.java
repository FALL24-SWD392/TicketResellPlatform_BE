package com.swd392.ticket_resell_be.dtos.requests;


import com.swd392.ticket_resell_be.enums.Categorize;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public record TicketDtoRequest(
        @NotEmpty(message = "SELLER_ID_EMPTY")
        UUID seller_id,

        @NotEmpty(message = "TITLE_EMPTY")
        String title,

        @NotEmpty(message = "TICKET_EXPIRATION_EMPTY")
        Date exp_date,

        @NotEmpty(message = "TICKET_TYPE_EMPTY")
        Categorize type,

        @NotEmpty(message = "SALE_PRICE_EMPTY")
        @DecimalMin(value = "0.0", inclusive = true, message = "SALE_PRICE_NEGATIVE")
        @DecimalMax(value = "1000000000.0", inclusive = true, message = "SALE_PRICE_TOO_HIGH")
        BigDecimal unit_price,

        @NotEmpty(message = "QUANTITY_EMPTY")
        int quantity,

        @NotEmpty(message = "DESCRIPTION_EMPTY")
        String description,

        @NotEmpty(message = "IMAGE_EMPTY")
        String image,

        @NotEmpty(message = "STATUS_EMPTY")
        Categorize status,

        @NotEmpty(message = "CREATED_AT_EMPTY")
        Date created_at,

        @NotEmpty(message = "UPDATED_BY_EMPTY")
        UUID updated_by,

        @NotEmpty(message = "UPDATED_AT_EMPTY")
        Date updated_at
) implements Serializable {
}
