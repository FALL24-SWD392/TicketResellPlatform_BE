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
public class TicketDtoResponse {
        private UUID id;
        private UUID sellerId;
        private String title;
        private Date expDate;
        private Categorize type;
        private float unitPrice;
        private int quantity;
        private String description;
        private String image;
        private Categorize status;
        private Date createdAt;
        private String updatedBy;
        private Date updatedAt;
}
