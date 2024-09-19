package com.swd392.ticket_resell_be.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Map;

public record PackageDtoRequest(
        @Size(max = 50)
        @NotEmpty
        String packageName,
        Integer saleLimit,
        @NotEmpty
        BigDecimal price,
        Map<String, Object> imageUrls
) {
}
