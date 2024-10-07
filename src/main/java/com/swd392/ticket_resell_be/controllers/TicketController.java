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

    @GetMapping("/view-all-tickets")
    public ResponseEntity<ApiListResponse<TicketDtoResponse>> viewAllTickets() {
        return ResponseEntity.ok(ticketService.viewAllTickets());
    }

    @GetMapping("/view-tickets-by-category")
    public ResponseEntity<ApiListResponse<TicketDtoResponse>> viewTicketsByCategory(
            @RequestParam @Valid Categorize category) {
        return ResponseEntity.ok(ticketService.viewTicketsByCategory(category));
    }


    @GetMapping("/view-tickets-by-name")
    public ResponseEntity<ApiListResponse<TicketDtoResponse>> viewTicketsByName(
            @RequestParam @Valid String name) {
        return ResponseEntity.ok(ticketService.getByName(name));
    }

    @GetMapping("/view-all-tickets-for-admin")
    public ResponseEntity<ApiListResponse<TicketDtoResponse>> viewAllTicketsForAdmin() {
        return ResponseEntity.ok(ticketService.viewAllTicketsForAdmin());
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiListResponse<Categorize>> getAllCategory() {
        return ResponseEntity.ok(ticketService.getAllCategory());
    }


    @GetMapping("/view-ticket-by-id")
    public ResponseEntity<ApiItemResponse<TicketDtoResponse>> viewTicketById(
            @RequestParam UUID id) {
        return ResponseEntity.ok(ticketService.viewTicketById(id));
    }
}
