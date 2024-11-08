package com.swd392.ticket_resell_be.dtos.responses;

import com.swd392.ticket_resell_be.enums.Categorize;

import java.util.Date;
import java.util.UUID;

public record TicketDtoIdResponse(
        UUID id,
        UUID sellerId,
        String avatar,
        String username,
        float rating,
        String title,
        Date expDate,
        Categorize type,
        float unitPrice,
        int quantity,
        String description,
        String image,
        Categorize status,
        Date createdAt,
        String updatedBy,
        Date updatedAt
) {
}
