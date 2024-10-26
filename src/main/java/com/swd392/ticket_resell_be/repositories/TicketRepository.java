package com.swd392.ticket_resell_be.repositories;


import com.swd392.ticket_resell_be.entities.Ticket;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.Categorize;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;


public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    Page<Ticket> findTicketByStatus(Categorize status, Pageable pageable);

    Page<Ticket> findTicketByTypeAndStatus(Categorize categorize, Categorize status, Pageable pageable);

    Page<Ticket> findTicketByTitleContainingAndStatus(String name, Categorize status, Pageable pageable);

    @Query("SELECT t FROM Ticket t JOIN FETCH t.seller WHERE t.id = :id")
    Ticket findTicketWithSellerById(@Param("id") UUID id);

    Page<Ticket> findTicketByTypeAndStatusAndTitle(Categorize type, Categorize status, String name, Pageable page);

    int countBySellerAndStatus(@NotNull User seller, @NotNull Categorize status);
}
