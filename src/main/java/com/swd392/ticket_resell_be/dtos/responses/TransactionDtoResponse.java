package com.swd392.ticket_resell_be.dtos.responses;

import com.swd392.ticket_resell_be.enums.Categorize;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class TransactionDtoResponse {
    private UUID id;
    private String orderCode;
    private Categorize status;
    private Date createdAt;
    private Date updatedAt;
    private String userName;
    private String description;

}
