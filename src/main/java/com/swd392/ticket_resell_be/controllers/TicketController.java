package com.swd392.ticket_resell_be.controllers;

import com.swd392.ticket_resell_be.dtos.requests.TicketDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.TicketDtoResponse;
import com.swd392.ticket_resell_be.entities.Ticket;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.services.TicketService;
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
@RequestMapping("/tickets")
@Tag(name = "Ticket APIs")
public class TicketController {
    TicketService ticketService;

    @PostMapping
    public ResponseEntity<ApiItemResponse<Ticket>> createTicket(
            @RequestBody @Valid TicketDtoRequest ticketDtoRequest) {
        return ResponseEntity.ok(ticketService.createTicket(ticketDtoRequest));
    }

    @PutMapping
    public ResponseEntity<ApiItemResponse<Ticket>> updateTicket(
            @RequestParam UUID id,
            @RequestBody @Valid TicketDtoRequest ticketDtoRequest) {
        return ResponseEntity.ok(ticketService.updateTicket(id, ticketDtoRequest));
    }

    @PutMapping("/process")
    public ResponseEntity<ApiItemResponse<Ticket>> processTicket(
            @RequestParam @Valid UUID id, Categorize status) {
        return ResponseEntity.ok(ticketService.processTicket(id, status));
    }

    @DeleteMapping
    public ResponseEntity<ApiItemResponse<Ticket>> removeTicket(
            @RequestParam UUID id) {
        return ResponseEntity.ok(ticketService.removeTicket(id));
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiListResponse<TicketDtoResponse>> viewAllTickets() {
        return ResponseEntity.ok(ticketService.viewAllTickets());
    }

    @GetMapping
    public ResponseEntity<ApiListResponse<TicketDtoResponse>> viewTicketsByCategoryAndName(
            @RequestParam @Valid Categorize category, String name) {
        return ResponseEntity.ok(ticketService.viewTicketsByCategoryAndName(category, name));
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @GetMapping("/admin/get-all")
    public ResponseEntity<ApiListResponse<TicketDtoResponse>> viewAllTicketsForAdmin() {
        return ResponseEntity.ok(ticketService.viewAllTicketsForAdmin());
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiListResponse<Categorize>> getAllCategory() {
        return ResponseEntity.ok(ticketService.getAllCategory());
    }


    @GetMapping
    public ResponseEntity<ApiItemResponse<TicketDtoResponse>> viewTicketById(
            @RequestParam UUID id) {
        return ResponseEntity.ok(ticketService.viewTicketById(id));
    }
}
