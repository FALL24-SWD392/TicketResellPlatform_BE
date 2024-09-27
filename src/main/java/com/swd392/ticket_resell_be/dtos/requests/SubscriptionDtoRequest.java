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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionDtoRequest {

        @Column(name = "name", nullable = false, length = 50)
        private String name;

        @Column(name = "sale_limit", nullable = false)
        private int saleLimit;

        @Column(name = "description", nullable = false)
        private String description;

        @Column(name = "point_required", nullable = false)
        private int pointRequired;

        @Column(name = "price", nullable = false, precision = 10, scale = 2)
        private int price;


}
