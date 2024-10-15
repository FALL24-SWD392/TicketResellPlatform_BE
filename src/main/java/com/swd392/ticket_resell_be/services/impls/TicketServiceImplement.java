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
import com.swd392.ticket_resell_be.services.TicketService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import com.swd392.ticket_resell_be.utils.PagingUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TicketServiceImplement implements TicketService {
    TicketRepository ticketRepository;
    ApiResponseBuilder apiResponseBuilder;
    PagingUtil pagingUtil;

    ModelMapper modelMapper;


    @Override
    public ApiItemResponse<Ticket> createTicket(TicketDtoRequest ticketDtoRequest) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);
        Ticket ticket = modelMapper.map(ticketDtoRequest, Ticket.class);
        ticket.setStatus(Categorize.PENDING);
        return apiResponseBuilder.buildResponse(
                ticketRepository.save(ticket),
                HttpStatus.CREATED
        );
    }

    @Override
    public ApiItemResponse<Ticket> updateTicket(UUID id, TicketDtoRequest ticketDtoRequest) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);
        Ticket existingTicket = ticketRepository.findTicketByIdIs(id);
        if (existingTicket.getStatus() == Categorize.REMOVED)
            throw new AppException(ErrorCode.TICKET_NOT_FOUND);

        modelMapper.map(ticketDtoRequest, existingTicket);
        existingTicket.setStatus(Categorize.PENDING);
        return apiResponseBuilder.buildResponse(
                ticketRepository.save(existingTicket),
                HttpStatus.OK
        );
    }

    @Override
    public ApiItemResponse<Ticket> processTicket(UUID id, Categorize status) {
        Ticket ticket = ticketRepository.findTicketByIdIs(id);

        if (ticket == null)
            throw new AppException(ErrorCode.TICKET_NOT_FOUND);
        else {
            ticket.setStatus(status);
            ticketRepository.save(ticket);
            return apiResponseBuilder.buildResponse(
                    ticket,
                    HttpStatus.OK
            );
        }
    }

    @Override
    public ApiItemResponse<Ticket> removeTicket(UUID id) {
        Ticket ticket = ticketRepository.findTicketByIdIs(id);
        ticket.setStatus(Categorize.REMOVED);
        return apiResponseBuilder.buildResponse(
                ticketRepository.save(ticket),
                HttpStatus.OK
        );
    }

    private List<TicketDtoResponse> parseToTicketDtoResponse(Page<Ticket> tickets) {
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
                    parseToTicketDtoResponse(tickets),
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
        if (category == null && name == null) {
            tickets = ticketRepository.findAllByStatus(Categorize.APPROVED, pagingUtil.getPageable(pageDtoRequest));
        } else if (category == Categorize.ALL) {
            tickets = ticketRepository.findAllByStatus(Categorize.APPROVED, pagingUtil.getPageable(pageDtoRequest));
        } else {
            tickets = ticketRepository.findTicketByTypeAndStatusAndTitle(category, Categorize.APPROVED, name, pagingUtil.getPageable(pageDtoRequest));
        }
        return apiResponseBuilder.buildResponse(
                parseToTicketDtoResponse(tickets),
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
        return apiResponseBuilder.buildResponse(
                ticketDtoResponse,
                HttpStatus.OK
        );
    }

    @Override
    public int getCountBySellerAndStatus(User seller, Categorize status) {
        return ticketRepository.countBySellerAndStatus(seller, status);
    }
}
