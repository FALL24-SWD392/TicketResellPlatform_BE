package com.swd392.ticket_resell_be.dtos.responses;

import com.swd392.ticket_resell_be.entities.OrderDetail;
import com.swd392.ticket_resell_be.entities.Ticket;
import com.swd392.ticket_resell_be.enums.Categorize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDtoResponse {
    private UUID id;
    private String chatBoxId;
    private Categorize status;
    private Ticket ticket;
    private int quantity;
}
