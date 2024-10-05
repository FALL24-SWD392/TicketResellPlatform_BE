package com.swd392.ticket_resell_be.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;
@Builder
@Getter
@Setter
public class MembershipDtoResponse {
    private UUID id;
    private String subscriptionName;
    private int saleRemain;
    private Date startDate;
    private Date endDate;
}
