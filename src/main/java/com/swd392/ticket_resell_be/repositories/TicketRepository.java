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

import java.util.Date;
import java.util.List;
import java.util.UUID;


public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    Page<Ticket> findByTitleContainingIgnoreCaseAndStatus(String name, Categorize status, Pageable pageable);

    Page<Ticket> findByTitleContainingIgnoreCaseAndTypeAndStatus(String name, Categorize type, Categorize status, Pageable page);

    Page<Ticket> findByTitleContainingIgnoreCaseAndType(String title, Categorize type, Pageable page);

    Page<Ticket> findByTitleContainingIgnoreCase(String title, Pageable page);

    @Query("SELECT t FROM Ticket t JOIN FETCH t.seller WHERE t.id = :id")
    Ticket findTicketWithSellerById(@Param("id") UUID id);

    int countBySellerAndStatus(@NotNull User seller, @NotNull Categorize status);

    Page<Ticket> findBySeller(User seller, Pageable page);

    List<Ticket> findByStatusAndExpDateBefore(Categorize status, Date expDate);
}
