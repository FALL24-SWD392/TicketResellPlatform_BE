package com.swd392.ticket_resell_be.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PackagePurchaseRequest {
    private UUID packageId; // Package ID
}
