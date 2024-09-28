package com.swd392.ticket_resell_be.services;


import com.swd392.ticket_resell_be.dtos.requests.TicketDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.entities.Report;
import com.swd392.ticket_resell_be.entities.Ticket;
import com.swd392.ticket_resell_be.enums.Categorize;

import java.util.UUID;


public interface TicketService {
    ApiItemResponse<Ticket> createTicket(TicketDtoRequest ticketDtoRequest);


    ApiItemResponse<Ticket> updateTicket(UUID id, TicketDtoRequest ticketDtoRequest);

    ApiItemResponse<Ticket> processTicket(UUID id, Categorize status);

    ApiItemResponse<Ticket> removeTicket(UUID id);

    ApiListResponse<Ticket> getByStatus(Categorize status);

    ApiListResponse<Ticket> getByNameAndStatus(String name, Categorize status);

    ApiListResponse<Ticket> viewAllTickets(Categorize status);

    ApiListResponse<Ticket> viewTicketsByCategory(Categorize category, Categorize status);

    ApiListResponse<Categorize> getAllCategory();

    ApiItemResponse<Ticket> viewTicketById(UUID id);
}
