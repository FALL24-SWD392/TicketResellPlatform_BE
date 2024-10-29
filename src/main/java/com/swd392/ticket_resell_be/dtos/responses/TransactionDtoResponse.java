package com.swd392.ticket_resell_be.dtos.responses;

import com.swd392.ticket_resell_be.enums.Categorize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDtoResponse {
    private String orderCode;
    private Categorize status;
    private Date createdAt;
    private Date updatedAt;
    private String userName;
    private String description;


}
