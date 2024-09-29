package com.swd392.ticket_resell_be.dtos.requests;

public record SubscriptionDtoRequest(String name, int saleLimit, String description,
                                     int pointRequired, int price
) {
}
