package com.swd392.ticket_resell_be.services;

import com.swd392.ticket_resell_be.dtos.requests.TransactionDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.entities.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionService {

    ApiItemResponse<Transaction> saveTransaction(TransactionDtoRequest transactionDtoRequest);
    ApiItemResponse<Optional<Transaction>> getTransactionById(UUID transactionId);
    ApiItemResponse<List<Transaction>> getAllTransactions();
    ApiItemResponse<Transaction> updateTransaction(UUID transactionId, TransactionDtoRequest transactionDtoRequest);
    ApiItemResponse<Void> deleteTransaction(UUID transactionId);
}
