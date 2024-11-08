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
public class ReportDtoResponse {
    private UUID id;
    private String reporterName;
    private String reportedName;
    private UUID ticketId;
    private String ticketName;
    private String description;
    private String attachment;
    private Categorize status;
    private String createdBy;
    private Date createdAt;
    private String updatedBy;
    private Date updatedAt;
}
