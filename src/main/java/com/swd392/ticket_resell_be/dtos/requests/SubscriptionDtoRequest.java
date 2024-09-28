package com.swd392.ticket_resell_be.dtos.requests;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

public record SubscriptionDtoRequest(String name, int saleLimit, String description,
                                     int pointRequired, int price
) {
}
