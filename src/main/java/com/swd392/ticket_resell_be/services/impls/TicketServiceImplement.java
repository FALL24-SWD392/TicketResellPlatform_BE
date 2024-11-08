package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.requests.TicketDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.TicketDtoIdResponse;
import com.swd392.ticket_resell_be.dtos.responses.TicketDtoResponse;
import com.swd392.ticket_resell_be.entities.Ticket;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.TicketRepository;
import com.swd392.ticket_resell_be.services.MembershipService;
import com.swd392.ticket_resell_be.services.TicketService;
import com.swd392.ticket_resell_be.services.UserService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import com.swd392.ticket_resell_be.utils.PagingUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TicketServiceImplement implements TicketService {
    TicketRepository ticketRepository;
    UserService userService;
    MembershipService membershipService;

    ApiResponseBuilder apiResponseBuilder;
    PagingUtil pagingUtil;

    @Override
    public ApiItemResponse<TicketDtoResponse> createTicket(TicketDtoRequest ticketDtoRequest) {
        if (checkSaleRemain(ticketDtoRequest))
            throw new AppException(ErrorCode.YOU_SELL_MAXIMUM_TICKET);
        else {
            Ticket ticket = new Ticket();
            mapperHandmade(ticket, ticketDtoRequest);
            ticket.setStatus(Categorize.PENDING);
            ticketRepository.save(ticket);
            return apiResponseBuilder.buildResponse(
                    parseToTicketDtoResponse(ticket),
                    HttpStatus.CREATED
            );
        }
    }

    private boolean checkSaleRemain(TicketDtoRequest ticketDtoRequest) {
        User user = userService.findById(ticketDtoRequest.sellerId());
        int countTicket = getCountBySellerAndStatus(user, Categorize.APPROVED) + getCountBySellerAndStatus(user, Categorize.PENDING);
        return membershipService.getMembershipForLoggedInUser(user).getSaleRemain() - countTicket <= 0;
    }

    @Override
    public ApiItemResponse<TicketDtoResponse> updateTicket(UUID id, TicketDtoRequest ticketDtoRequest) {
        Ticket existingTicket = ticketRepository.findTicketWithSellerById(id);
        if (existingTicket.getStatus() == Categorize.REMOVED)
            throw new AppException(ErrorCode.TICKET_NOT_FOUND);
        mapperHandmade(existingTicket, ticketDtoRequest);
        existingTicket.setId(id);
        existingTicket.setStatus(Categorize.PENDING);
        ticketRepository.save(existingTicket);
        return apiResponseBuilder.buildResponse(
                parseToTicketDtoResponse(existingTicket),
                HttpStatus.OK
        );
    }

    private void mapperHandmade(Ticket ticket, TicketDtoRequest ticketDtoRequest) {
        if (userService.findById(ticketDtoRequest.sellerId()) != null) {
            ticket.setSeller(userService.findById(ticketDtoRequest.sellerId()));
            ticket.setTitle(ticketDtoRequest.title());
            ticket.setExpDate(ticketDtoRequest.expDate());
            ticket.setType(ticketDtoRequest.type());
            ticket.setUnitPrice(ticketDtoRequest.unitPrice().floatValue());
            ticket.setQuantity(ticketDtoRequest.quantity());
            ticket.setDescription(ticketDtoRequest.description());
            ticket.setImage(ticketDtoRequest.image());
            ticket.setStatus(ticketDtoRequest.status());
        }
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

        if (ticket.getStatus().equals(Categorize.REMOVED))
            throw new AppException(ErrorCode.TICKET_ALREADY_REMOVED);
        else {
            ticket.setStatus(Categorize.REMOVED);
            ticketRepository.save(ticket);
            return apiResponseBuilder.buildResponse(
                    parseToTicketDtoResponse(ticket),
                    HttpStatus.OK
            );
        }
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
    public ApiListResponse<TicketDtoResponse> viewAllTicketsForAdmin(String title, Categorize type, int page, int size, Sort.Direction direction, String... properties) {
        Page<Ticket> tickets;
        if (type.equals(Categorize.ALL))
            tickets = ticketRepository.findByTitleContainingIgnoreCase(title, pagingUtil
                    .getPageable(Ticket.class, page, size, direction, properties));
        else
            tickets = ticketRepository.findByTitleContainingIgnoreCaseAndType(title, type, pagingUtil
                    .getPageable(Ticket.class, page, size, direction, properties));
        if (tickets.isEmpty())
            throw new AppException(ErrorCode.TICKET_NOT_FOUND);
        else
            return apiResponseBuilder.buildResponse(
                    parseToTicketDtoResponses(tickets),
                    tickets.getSize(),
                    tickets.getNumber() + 1,
                    tickets.getTotalElements(),
                    tickets.getTotalPages(),
                    HttpStatus.OK,
                    null
            );
    }

    @Override
    public ApiListResponse<TicketDtoResponse> viewTicketsByCategoryAndName(String name, Categorize type, int page, int size, Sort.Direction direction, String... properties) {
        Page<Ticket> tickets;
        if (type.equals(Categorize.ALL))
            tickets = ticketRepository.findByTitleContainingIgnoreCaseAndStatus(name, Categorize.APPROVED, pagingUtil
                    .getPageable(Ticket.class, page, size, direction, properties));
        else
            tickets = ticketRepository.findByTitleContainingIgnoreCaseAndTypeAndStatus(name, type, Categorize.APPROVED, pagingUtil
                    .getPageable(Ticket.class, page, size, direction, properties));
        List<TicketDtoResponse> returnTickets = parseToTicketDtoResponses(tickets);

        return apiResponseBuilder.buildResponse(
                returnTickets,
                tickets.getSize(),
                tickets.getNumber() + 1,
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
    public ApiItemResponse<TicketDtoIdResponse> viewTicketById(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TICKET_NOT_FOUND));
        return apiResponseBuilder.buildResponse(
                parseToTicketDtoIdResponse(ticket),
                HttpStatus.OK
        );
    }

    private TicketDtoIdResponse parseToTicketDtoIdResponse(Ticket ticket) {
        return new TicketDtoIdResponse(
                ticket.getId(),
                ticket.getSeller().getId(),
                ticket.getSeller().getAvatar(),
                ticket.getSeller().getUsername(),
                ticket.getSeller().getRating(),
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
                ticket.getUpdatedAt()
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

    @Override
    public Ticket getTicketById(UUID id) {
        return ticketRepository.findTicketWithSellerById(id);
    }

    @Override
    public void updateTicketStatus(UUID id, Categorize status) {
        Ticket ticket = ticketRepository.findTicketWithSellerById(id);
        ticket.setStatus(status);
        ticketRepository.save(ticket);
    }

    @Override
    public void updateTicketQuantity(UUID id, int quantity) {
        Ticket ticket = ticketRepository.findTicketWithSellerById(id);
        ticket.setQuantity(quantity);
        ticketRepository.save(ticket);
    }

    @Override
    public ApiListResponse<TicketDtoResponse> viewTicketByUserId(UUID userId, int page, int size, Sort.Direction direction, String[] properties) {
        Page<Ticket> tickets = ticketRepository.findBySeller(userService.findById(userId), pagingUtil
                .getPageable(Ticket.class, page, size, direction, properties));
        List<TicketDtoResponse> returnTickets = parseToTicketDtoResponses(tickets);

        return apiResponseBuilder.buildResponse(
                returnTickets,
                tickets.getSize(),
                tickets.getNumber() + 1,
                tickets.getTotalElements(),
                tickets.getTotalPages(),
                HttpStatus.OK
        );
    }

    @Override
    public void updateExpiredTicket() {
        List<Ticket> tickets = ticketRepository.findByStatusAndExpDateBefore(Categorize.APPROVED,
                Date.from(Instant.now()));
        tickets.forEach(ticket -> ticket.setStatus(Categorize.EXPIRED));
        ticketRepository.saveAll(tickets);
    }
}
