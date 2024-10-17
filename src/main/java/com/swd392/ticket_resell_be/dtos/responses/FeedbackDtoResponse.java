package com.swd392.ticket_resell_be.dtos.responses;

import com.swd392.ticket_resell_be.enums.Categorize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDtoResponse {
    private UUID id;
    private UUID buyerId;
    private UUID orderId;
    private String description;
    private int rating;
    private Categorize status;
    private Date createdAt;
    private Date updatedAt;
}
