package com.swd392.ticket_resell_be.services;


import com.swd392.ticket_resell_be.dtos.requests.PageDtoRequest;
import com.swd392.ticket_resell_be.dtos.requests.ReportDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.ReportDtoResponse;
import com.swd392.ticket_resell_be.entities.Report;
import com.swd392.ticket_resell_be.enums.Categorize;

import java.util.UUID;


public interface ReportService {
    ApiItemResponse<Report> createReport(ReportDtoRequest reportDtoRequest);

    ApiItemResponse<Report> processReport(UUID id, Categorize status);

    ApiItemResponse<Report> getById(UUID id);

    ApiListResponse<ReportDtoResponse> getReportByUserId(UUID id, Categorize status, PageDtoRequest pageDtoRequest);

    ApiListResponse<ReportDtoResponse> getAllReportsByStatus(Categorize status, PageDtoRequest pageDtoRequest);
}
