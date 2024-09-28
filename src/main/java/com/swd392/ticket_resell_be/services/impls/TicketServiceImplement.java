package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.requests.TicketDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.entities.Ticket;
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
                HttpStatus.CREATED,
                null
        );
    }

    @Override
    public ApiItemResponse<Ticket> updateTicket(UUID id, TicketDtoRequest ticketDtoRequest) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);

        try {
            Ticket existingTicket = ticketRepository.findTicketByIdIs(id);
            if (existingTicket.getStatus() == Categorize.REMOVED)
                throw new AppException(ErrorCode.TICKET_NOT_FOUND);

            modelMapper.map(ticketDtoRequest, existingTicket);
            existingTicket.setStatus(Categorize.PENDING);
            return apiResponseBuilder.buildResponse(
                    ticketRepository.save(existingTicket),
                    HttpStatus.OK,
                    null
            );
        } catch (AppException e){
            return apiResponseBuilder.buildResponse(
                    null,
                    HttpStatus.NOT_FOUND,
                    "Error: " + e.getMessage()
            );
        }

    }

    @Override
    public ApiItemResponse<Ticket> processTicket(UUID id, Categorize status) {
        Ticket ticket = ticketRepository.findTicketByIdIs(id);
        try {
            if (ticket == null)
                throw new AppException(ErrorCode.TICKET_NOT_FOUND);
            else {
                ticket.setStatus(status);
                ticketRepository.save(ticket);
                return apiResponseBuilder.buildResponse(
                        ticket,
                        HttpStatus.OK,
                        null
                );
            }
        } catch (AppException e){
            return apiResponseBuilder.buildResponse(
                    null,
                    HttpStatus.NOT_FOUND,
                    "Error: " + e.getMessage()
            );
        }
    }


    @Override
    public ApiItemResponse<Ticket> removeTicket(UUID id) {
        Ticket ticket = ticketRepository.findTicketByIdIs(id);
        ticket.setStatus(Categorize.REMOVED);
        return apiResponseBuilder.buildResponse(
                ticketRepository.save(ticket),
                HttpStatus.OK,
                null
        );
    }

    @Override
    public ApiListResponse<Ticket> getByStatus(Categorize status) {
        List<Ticket> ticketList = ticketRepository.findTicketByStatus(status);
        try{
            if(ticketList.isEmpty())
                throw new AppException(ErrorCode.TICKET_NOT_FOUND);
            else
                return apiResponseBuilder.buildResponse(
                        ticketList,
                        0,
                        0,
                        0,
                        0,
                        HttpStatus.OK,
                        null
                );
        } catch (AppException e){
            return apiResponseBuilder.buildResponse(
                    null,
                    0,
                    0,
                    0,
                    0,
                    HttpStatus.NOT_FOUND,
                    "Error: " + e.getMessage()
            );
        }
    }

    @Override
    public ApiListResponse<Ticket> viewAllTickets(Categorize status) {
        List<Ticket> ticketList = ticketRepository.findAllByStatus(status);
        return apiResponseBuilder.buildResponse(
                ticketList,
                0,
                0,
                0,
                0,
                HttpStatus.OK,
                null
        );
    }


    @Override
    public ApiListResponse<Ticket> viewTicketsByCategory(Categorize category, Categorize status) {
        List<Ticket> ticketList;
        if (category == Categorize.ALL) {
            ticketList = ticketRepository.findAllByStatus(Categorize.APPROVED);
        } else {
            ticketList = ticketRepository.findTicketByTypeAndStatus(category, Categorize.APPROVED);
        }
        return apiResponseBuilder.buildResponse(
                ticketList,
                0,
                0,
                0,
                0,
                HttpStatus.OK,
                null
        );
    }


    @Override
    public ApiListResponse<Ticket> getByNameAndStatus(String name, Categorize status) {
        List<UUID> uuidList = ticketRepository.findTicketsByTitleLike("%" + name + "%")
                .stream()
                .map(Ticket::getId)
                .toList();
        List<Ticket> ticketList = ticketRepository.findAllById(uuidList);
        List<Ticket> returnList = new ArrayList<>();
        for(Ticket ticket : ticketList) {
            if(ticket.getStatus() == status) {
                returnList.add(ticket);
            }
        }
        return apiResponseBuilder.buildResponse(
                returnList,
                0,
                0,
                0,
                0,
                HttpStatus.OK,
                null
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
                HttpStatus.OK,
                null
        );
    }


    @Override
    public ApiItemResponse<Ticket> viewTicketById(UUID id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.TICKET_NOT_FOUND));
        return apiResponseBuilder.buildResponse(
                ticket,
                HttpStatus.OK,
                null
        );
    }
}
