package com.swd392.ticket_resell_be.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDtoResponse {

    private UUID id;
    private String name;
    private int saleLimit;
    private String description;
    private int pointRequired;
    private float price;
    private boolean canPurchase;
}
