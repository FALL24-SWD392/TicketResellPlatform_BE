package com.swd392.ticket_resell_be.controllers;

import com.swd392.ticket_resell_be.dtos.requests.ReportDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.entities.Report;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.services.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/reports")
@Tag(name = "Report APIs")
public class ReportController {
    ReportService reportService;

    @PostMapping
    public ResponseEntity<ApiItemResponse<Report>> createReport(
            @RequestBody @Valid ReportDtoRequest reportDtoRequest) {
        return ResponseEntity.ok(reportService.createReport(reportDtoRequest));
    }

    @PutMapping("/process")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiItemResponse<Report>> processReport(
            @RequestBody @Valid UUID id, Categorize status) {
        return ResponseEntity.ok(reportService.processReport(id, status));
    }

    @GetMapping("/id")
    public ResponseEntity<ApiItemResponse<Report>> getById(
            @RequestParam @Valid UUID id) {
        return ResponseEntity.ok(reportService.getById(id));
    }

    @GetMapping("/user-id")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiListResponse<Report>> getReportByUserId(
            @RequestParam @Valid UUID userId, Categorize status) {
        return ResponseEntity.ok(reportService.getReportByUserId(userId, status));
    }

    @GetMapping("/status")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiListResponse<Report>> getReportByStatus(
            @RequestParam @Valid Categorize status) {
        return ResponseEntity.ok(reportService.getAllReportsByStatus(status));
    }
}
