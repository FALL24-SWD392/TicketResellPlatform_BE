package com.swd392.ticket_resell_be.dtos.responses;

import com.swd392.ticket_resell_be.enums.Categorize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDtoResponse {
    private UUID id;
    private UUID orderId;
    Categorize status;
}
