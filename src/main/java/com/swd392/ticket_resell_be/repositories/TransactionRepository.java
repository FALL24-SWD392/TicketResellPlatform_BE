package com.swd392.ticket_resell_be.repositories;

import com.swd392.ticket_resell_be.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Optional<Transaction> findById(UUID transactionId);
    List<Transaction> findAll();

    Optional<Object> findTransactionsByDescription(String orderCode);
}
