package com.swd392.ticket_resell_be.dtos.requests;


import com.swd392.ticket_resell_be.enums.Categorize;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public record TicketDtoRequest(
        @NotNull(message = "SELLER_ID_MUST_NOT_BE_NULL")
        UUID sellerId,

        @NotBlank(message = "TITLE_EMPTY")
        String title,

        @NotNull(message = "TICKET_EXPIRATION_MUST_NOT_BE_NULL")
        @Future(message = "EXPIRATION_DATE_MUST_BE_IN_THE_FUTURE")
        Date expDate,

        @NotNull(message = "TYPE_MUST_NOT_BE_NULL")
        Categorize type,

        @NotNull(message = "SALE_PRICE_MUST_NOT_BE_NULL")
        @DecimalMin(value = "0.0", inclusive = true, message = "SALE_PRICE_NEGATIVE")
        @DecimalMax(value = "1000000000.0", inclusive = true, message = "SALE_PRICE_TOO_HIGH")
        BigDecimal unitPrice,

        @NotNull(message = "QUANTITY_MUST_NOT_BE_NULL")
        @Positive(message = "Quantity must be greater than 0")
        Integer quantity,

        @NotBlank(message = "DESCRIPTION_EMPTY")
        String description,

        @NotBlank(message = "IMAGE_EMPTY")
        String image,

        Categorize status
) implements Serializable {
}

