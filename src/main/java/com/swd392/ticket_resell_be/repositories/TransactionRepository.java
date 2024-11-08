package com.swd392.ticket_resell_be.repositories;

import com.swd392.ticket_resell_be.entities.Transaction;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.Categorize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Optional<Transaction> getTransactionByOrderCode(String orderCode);

    Page<Transaction> findByDescriptionContainsIgnoreCase(String description, Pageable pageable);

    Page<Transaction> findByDescriptionContainsIgnoreCaseAndStatus(String description, Categorize status, Pageable pageable);

    Page<Transaction> findBySeller(User user, Pageable pageable);

    List<Transaction> findByStatusAndUpdatedAtBefore(Categorize status, Date updatedAt);
}
