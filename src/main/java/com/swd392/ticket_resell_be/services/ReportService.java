package com.swd392.ticket_resell_be.services;


import com.swd392.ticket_resell_be.dtos.requests.ReportDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.ReportDtoResponse;
import com.swd392.ticket_resell_be.enums.Categorize;
import org.springframework.data.domain.Sort;

import java.util.UUID;


public interface ReportService {
    ApiItemResponse<ReportDtoResponse> createReport(ReportDtoRequest reportDtoRequest);

    ApiItemResponse<ReportDtoResponse> processReport(UUID id, Categorize status);

    ApiItemResponse<ReportDtoResponse> getById(UUID id);

    ApiListResponse<ReportDtoResponse> getReportByUserId(UUID id, int page, int size, Sort.Direction direction, String... properties);

    ApiListResponse<ReportDtoResponse> getAllReportsByStatus(Categorize status, int page, int size, Sort.Direction direction, String... properties);
}
