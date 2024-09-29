package com.swd392.ticket_resell_be.controllers;

import com.swd392.ticket_resell_be.dtos.requests.ReportDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.entities.Report;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.services.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/report")
public class ReportController {
    ReportService reportService;

    @PostMapping("/create")
    public ResponseEntity<ApiItemResponse<Report>> createReport(
            @RequestBody @Valid ReportDtoRequest reportDtoRequest) {
        return ResponseEntity.ok(reportService.createReport(reportDtoRequest));
    }

    @PutMapping("/process")
    public ResponseEntity<ApiItemResponse<Report>> processReport(
            @RequestBody @Valid UUID id, Categorize status) {
        return ResponseEntity.ok(reportService.processReport(id, status));
    }

    @GetMapping("/view-by-id")
    public ResponseEntity<ApiItemResponse<Report>> getById(
            @RequestBody @Valid UUID id) {
        return ResponseEntity.ok(reportService.getById(id));
    }

    @GetMapping("/view-by-user-id")
    public ResponseEntity<ApiListResponse<Report>> getReportByUserId(
            @RequestParam @Valid UUID id, Categorize status) {
        return ResponseEntity.ok(reportService.getReportByUserId(id, status));
    }

    @GetMapping("/view-by-status")
    public ResponseEntity<ApiListResponse<Report>> getReportByStatus(
            @RequestParam @Valid Categorize status) {
        return ResponseEntity.ok(reportService.getAllReportsByStatus(status));
    }
}
