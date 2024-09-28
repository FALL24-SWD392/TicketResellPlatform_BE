package com.swd392.ticket_resell_be.controllers;

import com.swd392.ticket_resell_be.dtos.requests.TicketDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.entities.Ticket;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.services.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/ticket")
public class TicketController {
    TicketService ticketService;


    @PostMapping("/create")
    public ResponseEntity<ApiItemResponse<Ticket>> createTicket(
            @RequestBody @Valid TicketDtoRequest ticketDtoRequest) {
        return ResponseEntity.ok(ticketService.createTicket(ticketDtoRequest));
    }


    @PutMapping("/update")
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

    @DeleteMapping("/remove")
    public ResponseEntity<ApiItemResponse<Ticket>> removeTicket(
            @RequestParam UUID id) {
        return ResponseEntity.ok(ticketService.removeTicket(id));
    }

    @GetMapping("/view-all-tickets")
    public ResponseEntity<ApiListResponse<Ticket>> viewAllTickets(
            @RequestBody @Valid Categorize status) {
        return ResponseEntity.ok(ticketService.viewAllTickets(status));
    }

    @GetMapping("/view-tickets-by-category")
    public ResponseEntity<ApiListResponse<Ticket>> viewTicketsByCategory(
            @RequestParam @Valid Categorize category, Categorize status) {
        return ResponseEntity.ok(ticketService.viewTicketsByCategory(category, status));
    }


    @GetMapping("/view-tickets-by-name")
    public ResponseEntity<ApiListResponse<Ticket>> viewTicketsByName(
            @RequestParam @Valid String name, Categorize status) {
        return ResponseEntity.ok(ticketService.getByNameAndStatus(name, status));
    }


    @GetMapping("/categories")
    public ResponseEntity<ApiListResponse<Categorize>> getAllCategory() {
        return ResponseEntity.ok(ticketService.getAllCategory());
    }


    @GetMapping("/view-ticket-by-id")
    public ResponseEntity<ApiItemResponse<Ticket>> viewTicketById(
            @RequestParam UUID id) {
        return ResponseEntity.ok(ticketService.viewTicketById(id));
    }
}
