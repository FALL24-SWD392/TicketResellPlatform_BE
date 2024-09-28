package com.swd392.ticket_resell_be.repositories;


import com.swd392.ticket_resell_be.entities.Ticket;
import com.swd392.ticket_resell_be.enums.Categorize;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findAllByStatus(Categorize status);

    Ticket findTicketByIdIs(UUID id);

    List<Ticket> findTicketsByTitleLike(String ticketName);

    List<Ticket> findTicketByStatus(Categorize status);

    List<Ticket> findTicketByTypeAndStatus(Categorize type, Categorize status);
}
