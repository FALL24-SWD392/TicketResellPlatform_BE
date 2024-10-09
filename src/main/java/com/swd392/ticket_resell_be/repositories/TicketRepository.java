package com.swd392.ticket_resell_be.repositories;


import com.swd392.ticket_resell_be.entities.Ticket;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.Categorize;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findAllByStatus(Categorize status);

    Ticket findTicketByIdIs(UUID id);

    List<Ticket> findTicketByTypeAndStatusAndTitle(Categorize type, Categorize status, String name);

    int countBySellerAndStatus(@NotNull User seller, @NotNull Categorize status);
}
