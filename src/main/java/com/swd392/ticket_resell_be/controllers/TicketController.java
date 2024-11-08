package com.swd392.ticket_resell_be.controllers;

import com.swd392.ticket_resell_be.dtos.requests.TicketDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.TicketDtoResponse;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.services.TicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/tickets")
@Tag(name = "Ticket APIs")
public class TicketController {
    TicketService ticketService;

    @PostMapping
    public ResponseEntity<ApiItemResponse<TicketDtoResponse>> createTicket(
            @RequestBody @Valid TicketDtoRequest ticketDtoRequest) {
        return ResponseEntity.ok(ticketService.createTicket(ticketDtoRequest));
    }

    @PutMapping
    public ResponseEntity<ApiItemResponse<TicketDtoResponse>> updateTicket(
            @RequestParam UUID id,
            @RequestBody @Valid TicketDtoRequest ticketDtoRequest) {
        return ResponseEntity.ok(ticketService.updateTicket(id, ticketDtoRequest));
    }

    @PutMapping("/process")
    public ResponseEntity<ApiItemResponse<TicketDtoResponse>> processTicket(
            @RequestParam @Valid UUID id, Categorize status) {
        return ResponseEntity.ok(ticketService.processTicket(id, status));
    }

    @DeleteMapping
    public ResponseEntity<ApiItemResponse<TicketDtoResponse>> removeTicket(
            @RequestParam UUID id) {
        return ResponseEntity.ok(ticketService.removeTicket(id));
    }

    @GetMapping
    public ResponseEntity<ApiListResponse<TicketDtoResponse>> viewTicketsByCategoryAndName(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "ALL") Categorize type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(defaultValue = "id") String... properties) {
        return ResponseEntity.ok(ticketService.viewTicketsByCategoryAndName(name, type, page - 1, size, direction, properties));
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/admin")
    public ResponseEntity<ApiListResponse<TicketDtoResponse>> viewAllTicketsForAdmin(
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "ALL") Categorize type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(defaultValue = "id") String... properties) {
        return ResponseEntity.ok(ticketService.viewAllTicketsForAdmin(title, type, page - 1, size, direction, properties));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiListResponse<Categorize>> getAllCategory() {
        return ResponseEntity.ok(ticketService.getAllCategory());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiItemResponse<TicketDtoResponse>> viewTicketById(
            @PathVariable("id") UUID id) {
        return ResponseEntity.ok(ticketService.viewTicketById(id));
    }

    @GetMapping("/user")
    public  ResponseEntity<ApiListResponse<TicketDtoResponse>> viewTicketByUserId(
            @RequestParam UUID userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(defaultValue = "id") String... properties) {
        return ResponseEntity.ok(ticketService.viewTicketByUserId(userId, page - 1, size, direction, properties));
    }
}
