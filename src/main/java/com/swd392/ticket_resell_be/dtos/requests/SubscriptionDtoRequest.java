package com.swd392.ticket_resell_be.dtos.requests;

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

        private UUID id;

        @Size(max = 50)
        @NotEmpty
        private String subscriptionName;

        private Integer saleLimit;

        @NotNull
        private Integer price;

        private Map<String, Object> imageUrls;

        @NotNull
        private Integer duration;

        @Builder.Default
        @NotNull
        private boolean active = true;

}
