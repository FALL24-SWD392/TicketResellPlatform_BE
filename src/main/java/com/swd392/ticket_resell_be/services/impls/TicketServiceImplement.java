package com.swd392.ticket_resell_be.services.impls;

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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TicketServiceImplement implements TicketService {
    TicketRepository ticketRepository;
    ApiResponseBuilder apiResponseBuilder;

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

    private List<TicketDtoResponse> parseToTicketDtoResponse(List<Ticket> ticketList) {
        List<TicketDtoResponse> ticketDtoResponses = new ArrayList<>();
        for (Ticket tick : ticketList) {
            TicketDtoResponse dto = new TicketDtoResponse(
                    tick.getId(),
                    tick.getSeller().getId(),
                    tick.getTitle(),
                    tick.getExpDate(),
                    tick.getType(),
                    tick.getUnitPrice(),
                    tick.getQuantity(),
                    tick.getDescription(),
                    tick.getImage(),
                    tick.getStatus(),
                    tick.getCreatedAt(),
                    tick.getUpdatedBy(),
                    tick.getUpdatedAt()
            );
            ticketDtoResponses.add(dto);
        }
        return ticketDtoResponses;
    }

    @Override
    public ApiListResponse<TicketDtoResponse> viewAllTickets() {
        List<Ticket> ticketList = ticketRepository.findAllByStatus(Categorize.APPROVED);
        if (ticketList.isEmpty())
            throw new AppException(ErrorCode.TICKET_NOT_FOUND);
        else
            return apiResponseBuilder.buildResponse(
                    parseToTicketDtoResponse(ticketList),
                    0,
                    0,
                    0,
                    0,
                    HttpStatus.OK
            );
    }

    @Override
    public ApiListResponse<TicketDtoResponse> viewAllTicketsForAdmin() {
        List<Ticket> ticketList = ticketRepository.findAll();
        if (ticketList.isEmpty())
            throw new AppException(ErrorCode.TICKET_NOT_FOUND);
        else
            return apiResponseBuilder.buildResponse(
                    parseToTicketDtoResponse(ticketList),
                    0,
                    0,
                    0,
                    0,
                    HttpStatus.OK
            );
    }

    @Override
    public ApiListResponse<TicketDtoResponse> viewTicketsByCategory(Categorize category) {
        List<Ticket> ticketList;
        if (category == Categorize.ALL) {
            ticketList = ticketRepository.findAllByStatus(Categorize.APPROVED);
        } else {
            ticketList = ticketRepository.findTicketByTypeAndStatus(category, Categorize.APPROVED);
        }
        return apiResponseBuilder.buildResponse(
                parseToTicketDtoResponse(ticketList),
                0,
                0,
                0,
                0,
                HttpStatus.OK
        );
    }

    @Override
    public ApiListResponse<TicketDtoResponse> getByName(String name) {
        List<UUID> uuidList = ticketRepository.findTicketsByTitleLike("%" + name + "%")
                .stream()
                .map(Ticket::getId)
                .toList();
        List<Ticket> ticketList = ticketRepository.findAllById(uuidList);
        List<Ticket> returnList = new ArrayList<>();
        for (Ticket ticket : ticketList) {
            if (ticket.getStatus() == Categorize.APPROVED) {
                returnList.add(ticket);
            }
        }
        return apiResponseBuilder.buildResponse(
                parseToTicketDtoResponse(returnList),
                0,
                0,
                0,
                0,
                HttpStatus.OK
        );
    }

    @Override
    public ApiListResponse<Categorize> getAllCategory() {
        return apiResponseBuilder.buildResponse(
                Arrays.asList(Categorize.values()),
                0,
                0,
                0,
                0,
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
