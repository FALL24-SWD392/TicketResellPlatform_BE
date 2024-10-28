package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.requests.PageDtoRequest;
import com.swd392.ticket_resell_be.dtos.requests.TicketDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.TicketDtoResponse;
import com.swd392.ticket_resell_be.entities.Ticket;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.TicketRepository;
import com.swd392.ticket_resell_be.repositories.UserRepository;
import com.swd392.ticket_resell_be.services.TicketService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import com.swd392.ticket_resell_be.utils.PagingUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TicketServiceImplement implements TicketService {
    TicketRepository ticketRepository;
    UserRepository userRepository;
    ApiResponseBuilder apiResponseBuilder;
    PagingUtil pagingUtil;

    @Override
    public ApiItemResponse<TicketDtoResponse> createTicket(TicketDtoRequest ticketDtoRequest) {
        Ticket ticket = new Ticket();
        ticket = mapperHandmade(ticket, ticketDtoRequest);
        ticket.setStatus(Categorize.PENDING);
        ticketRepository.save(ticket);
        return apiResponseBuilder.buildResponse(
                parseToTicketDtoResponse(ticket),
                HttpStatus.CREATED
        );
    }

    @Override
    public ApiItemResponse<TicketDtoResponse> updateTicket(UUID id, TicketDtoRequest ticketDtoRequest) {
        Ticket existingTicket = ticketRepository.findTicketWithSellerById(id);
        if (existingTicket.getStatus() == Categorize.REMOVED)
            throw new AppException(ErrorCode.TICKET_NOT_FOUND);
        existingTicket = mapperHandmade(existingTicket, ticketDtoRequest);
        existingTicket.setId(id);
        existingTicket.setStatus(Categorize.PENDING);
        ticketRepository.save(existingTicket);
        return apiResponseBuilder.buildResponse(
                parseToTicketDtoResponse(existingTicket),
                HttpStatus.OK
        );
    }

    private Ticket mapperHandmade(Ticket ticket, TicketDtoRequest ticketDtoRequest) {
        ticket.setSeller(userRepository.findById(ticketDtoRequest.seller_id()).get());
        ticket.setTitle(ticketDtoRequest.title());
        ticket.setExpDate(ticketDtoRequest.exp_date());
        ticket.setType(ticketDtoRequest.type());
        ticket.setUnitPrice(ticketDtoRequest.unit_price().floatValue());
        ticket.setQuantity(ticketDtoRequest.quantity());
        ticket.setDescription(ticketDtoRequest.description());
        ticket.setImage(ticketDtoRequest.image());
        ticket.setStatus(ticketDtoRequest.status());

        return ticket;
    }

    @Override
    public ApiItemResponse<TicketDtoResponse> processTicket(UUID id, Categorize status) {
        Ticket ticket = ticketRepository.findTicketWithSellerById(id);
        if (ticket == null)
            throw new AppException(ErrorCode.TICKET_NOT_FOUND);
        else {
            ticket.setStatus(status);
            ticketRepository.save(ticket);
            return apiResponseBuilder.buildResponse(
                    parseToTicketDtoResponse(ticket),
                    HttpStatus.OK
            );
        }
    }

    @Override
    public ApiItemResponse<TicketDtoResponse> removeTicket(UUID id) {
        Ticket ticket = ticketRepository.findTicketWithSellerById(id);
        ticket.setStatus(Categorize.REMOVED);
        ticketRepository.save(ticket);
        return apiResponseBuilder.buildResponse(
                parseToTicketDtoResponse(ticket),
                HttpStatus.OK
        );
    }

    private List<TicketDtoResponse> parseToTicketDtoResponses(Page<Ticket> tickets) {
        return tickets.getContent().stream()
                .map(ticket -> new TicketDtoResponse(
                        ticket.getId(),
                        ticket.getSeller().getId(),
                        ticket.getTitle(),
                        ticket.getExpDate(),
                        ticket.getType(),
                        ticket.getUnitPrice(),
                        ticket.getQuantity(),
                        ticket.getDescription(),
                        ticket.getImage(),
                        ticket.getStatus(),
                        ticket.getCreatedAt(),
                        ticket.getUpdatedBy(),
                        ticket.getUpdatedAt()))
                .toList();
    }

    @Override
    public ApiListResponse<TicketDtoResponse> viewAllTicketsForAdmin(PageDtoRequest pageDtoRequest) {
        Page<Ticket> tickets = ticketRepository.findAll(pagingUtil.getPageable(pageDtoRequest));
        if (tickets.isEmpty())
            throw new AppException(ErrorCode.TICKET_NOT_FOUND);
        else
            return apiResponseBuilder.buildResponse(
                    parseToTicketDtoResponses(tickets),
                    tickets.getSize(),
                    tickets.getNumber(),
                    tickets.getTotalElements(),
                    tickets.getTotalPages(),
                    HttpStatus.OK,
                    null
            );
    }

    @Override
    public ApiListResponse<TicketDtoResponse> viewTicketsByCategoryAndName(PageDtoRequest pageDtoRequest, Categorize category, String name) {
        Page<Ticket> tickets;
        Pageable page = pagingUtil.getPageable(pageDtoRequest);

        if (category != Categorize.ALL) {
            if (name.isEmpty())
                tickets = ticketRepository.findTicketByTypeAndStatus(category, Categorize.APPROVED, page);
            else
                tickets = ticketRepository.findTicketByTypeAndStatusAndTitle(category, Categorize.APPROVED, name, page);
        } else {
            if (name.isEmpty())
                tickets = ticketRepository.findTicketByStatus(Categorize.APPROVED, page);
            else
                tickets = ticketRepository.findTicketByTitleContainingAndStatus(name, Categorize.APPROVED, page);
        }

        List<TicketDtoResponse> returnTickets = parseToTicketDtoResponses(tickets);

        return apiResponseBuilder.buildResponse(
                returnTickets,
                tickets.getSize(),
                tickets.getNumber(),
                tickets.getTotalElements(),
                tickets.getTotalPages(),
                HttpStatus.OK
        );
    }

    @Override
    public ApiListResponse<Categorize> getAllCategory() {
        return apiResponseBuilder.buildResponse(
                Categorize.getByCategory("ticket"),
                Categorize.getByCategory("ticket").size(),
                1,
                Categorize.getByCategory("ticket").size(),
                1,
                HttpStatus.OK
        );
    }

    @Override
    public ApiItemResponse<TicketDtoResponse> viewTicketById(UUID id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.TICKET_NOT_FOUND));
        return apiResponseBuilder.buildResponse(
                parseToTicketDtoResponse(ticket),
                HttpStatus.OK
        );
    }

    private TicketDtoResponse parseToTicketDtoResponse(Ticket ticket) {
        TicketDtoResponse ticketDtoResponse = new TicketDtoResponse();
        ticketDtoResponse.setId(ticket.getId());
        ticketDtoResponse.setSellerId(ticket.getSeller().getId());
        ticketDtoResponse.setTitle(ticket.getTitle());
        ticketDtoResponse.setExpDate(ticket.getExpDate());
        ticketDtoResponse.setType(ticket.getType());
        ticketDtoResponse.setUnitPrice(ticket.getUnitPrice());
        ticketDtoResponse.setQuantity(ticket.getQuantity());
        ticketDtoResponse.setDescription(ticket.getDescription());
        ticketDtoResponse.setImage(ticket.getImage());
        ticketDtoResponse.setStatus(ticket.getStatus());
        ticketDtoResponse.setCreatedAt(ticket.getCreatedAt());
        ticketDtoResponse.setUpdatedBy(ticket.getUpdatedBy());
        ticketDtoResponse.setUpdatedAt(ticket.getUpdatedAt());

        return ticketDtoResponse;
    }

    @Override
    public int getCountBySellerAndStatus(User seller, Categorize status) {
        return ticketRepository.countBySellerAndStatus(seller, status);
    }
}
