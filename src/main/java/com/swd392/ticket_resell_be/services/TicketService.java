package com.swd392.ticket_resell_be.services;


import com.swd392.ticket_resell_be.dtos.requests.TicketDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.TicketDtoResponse;
import com.swd392.ticket_resell_be.entities.Ticket;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.Categorize;

import java.util.UUID;


public interface TicketService {
    ApiItemResponse<Ticket> createTicket(TicketDtoRequest ticketDtoRequest);


    ApiItemResponse<Ticket> updateTicket(UUID id, TicketDtoRequest ticketDtoRequest);

    ApiItemResponse<Ticket> processTicket(UUID id, Categorize status);

    ApiItemResponse<Ticket> removeTicket(UUID id);

    ApiListResponse<TicketDtoResponse> getByName(String name);

    ApiListResponse<TicketDtoResponse> viewAllTickets();

    ApiListResponse<TicketDtoResponse> viewAllTicketsForAdmin();

    ApiListResponse<TicketDtoResponse> viewTicketsByCategory(Categorize category);

    ApiListResponse<Categorize> getAllCategory();

    ApiItemResponse<TicketDtoResponse> viewTicketById(UUID id);

    int getCountBySellerAndStatus(User seller, Categorize status);
}
