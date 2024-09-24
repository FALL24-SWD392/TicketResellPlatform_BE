package com.swd392.ticket_resell_be.services;

import com.swd392.ticket_resell_be.entities.Package;
import com.swd392.ticket_resell_be.entities.Transaction;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.enums.TransactionStatus;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionService {
    void savePendingTransaction(Package pkg, User user, String orderId);
    Transaction getTransactionById(UUID transactionId) throws AppException;
    List<Transaction> getAllTransactions();
    Optional<Transaction> findTransactionByDescription(String orderCode) throws AppException;
    void updateTransactionStatus(UUID transactionId, TransactionStatus status) throws AppException;
}
