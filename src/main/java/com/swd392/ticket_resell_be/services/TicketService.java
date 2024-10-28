package com.swd392.ticket_resell_be.services;


import com.swd392.ticket_resell_be.dtos.requests.PageDtoRequest;
import com.swd392.ticket_resell_be.dtos.requests.TicketDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.TicketDtoResponse;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.Categorize;

import java.util.UUID;


public interface TicketService {
    ApiItemResponse<TicketDtoResponse> createTicket(TicketDtoRequest ticketDtoRequest);

    ApiItemResponse<TicketDtoResponse> updateTicket(UUID id, TicketDtoRequest ticketDtoRequest);

    ApiItemResponse<TicketDtoResponse> processTicket(UUID id, Categorize status);

    ApiItemResponse<TicketDtoResponse> removeTicket(UUID id);

    ApiListResponse<TicketDtoResponse> viewAllTicketsForAdmin(PageDtoRequest pageDtoRequest);

    ApiListResponse<TicketDtoResponse> viewTicketsByCategoryAndName(PageDtoRequest pageDtoRequest, Categorize category, String name);

    ApiListResponse<Categorize> getAllCategory();

    ApiItemResponse<TicketDtoResponse> viewTicketById(UUID id);

    int getCountBySellerAndStatus(User seller, Categorize status);
}
