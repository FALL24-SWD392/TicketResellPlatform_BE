package com.swd392.ticket_resell_be.dtos.requests;

import com.swd392.ticket_resell_be.enums.Categorize;

import java.util.UUID;

public record UpdateReportDtoRequest(UUID id, Categorize status) {
}
