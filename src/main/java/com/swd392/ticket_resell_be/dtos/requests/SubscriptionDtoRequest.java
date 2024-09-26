package com.swd392.ticket_resell_be.dtos.requests;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionDtoRequest {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "id", nullable = false, updatable = false)
        private UUID id;

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
