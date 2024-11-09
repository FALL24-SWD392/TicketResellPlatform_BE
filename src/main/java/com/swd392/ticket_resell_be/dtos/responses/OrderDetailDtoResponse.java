package com.swd392.ticket_resell_be.dtos.responses;

import com.swd392.ticket_resell_be.entities.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDtoResponse {
    private UUID id;
    private UUID orderId;
    private Ticket ticket;
    private int quantity;
}
