package com.swd392.ticket_resell_be.services;


import com.swd392.ticket_resell_be.dtos.requests.TicketDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.TicketDtoResponse;
import com.swd392.ticket_resell_be.entities.Ticket;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.Categorize;
import org.springframework.data.domain.Sort;

import java.util.UUID;


public interface TicketService {
    ApiItemResponse<TicketDtoResponse> createTicket(TicketDtoRequest ticketDtoRequest);

    ApiItemResponse<TicketDtoResponse> updateTicket(UUID id, TicketDtoRequest ticketDtoRequest);

    ApiItemResponse<TicketDtoResponse> processTicket(UUID id, Categorize status);

    ApiItemResponse<TicketDtoResponse> removeTicket(UUID id);

    ApiListResponse<TicketDtoResponse> viewAllTicketsForAdmin(String title, Categorize type, int page, int size, Sort.Direction direction, String... properties);

    ApiListResponse<TicketDtoResponse> viewTicketsByCategoryAndName(String name, Categorize type, int page, int size, Sort.Direction direction, String... properties);

    ApiListResponse<Categorize> getAllCategory();

    ApiItemResponse<TicketDtoResponse> viewTicketById(UUID id);

    int getCountBySellerAndStatus(User seller, Categorize status);

    Ticket getTicketById(UUID id);

    void updateTicketStatus(UUID id, Categorize status);

    void updateTicketQuantity(UUID id, int quantity);

    ApiListResponse<TicketDtoResponse> viewTicketByUserId(UUID userId, int i, int size, Sort.Direction direction, String[] properties);
}
