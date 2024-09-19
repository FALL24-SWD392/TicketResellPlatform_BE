package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.requests.SubscriptionDtoRequest;
import com.swd392.ticket_resell_be.dtos.requests.TransactionDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.entities.Package;
import com.swd392.ticket_resell_be.entities.Transaction;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.PackageRepository;
import com.swd392.ticket_resell_be.repositories.TransactionRepository;
import com.swd392.ticket_resell_be.services.SubscriptionService; // Import SubscriptionService
import com.swd392.ticket_resell_be.services.TransactionService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TransactionServiceImplement implements TransactionService {

    TransactionRepository transactionRepository;
    SubscriptionService subscriptionService; // Inject SubscriptionService
    PackageRepository packageRepository;
    ApiResponseBuilder apiResponseBuilder;

    @Override
    public ApiItemResponse<Transaction> saveTransaction(TransactionDtoRequest transactionDtoRequest) {
        // Create a new Transaction object
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setTransactionTime(OffsetDateTime.now());
        transaction.setAmount(transactionDtoRequest.amount());
        transaction.setTransactionType(transactionDtoRequest.transactionType());
        transaction.setDescription(transactionDtoRequest.description());
        // Fetch the Package based on the packageId in the request
        Package aPackage = packageRepository.findById(transactionDtoRequest.aPackage().getId())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_PACKAGE));
        Transaction savedTransaction = transactionRepository.save(transaction);
        SubscriptionDtoRequest subscriptionDtoRequest = new SubscriptionDtoRequest(transactionDtoRequest.user(), aPackage);
        subscriptionService.createSubscription(subscriptionDtoRequest);
        return apiResponseBuilder.buildResponse(savedTransaction, HttpStatus.CREATED, "Transaction created successfully and subscription activated.");
    }

    @Override
    public ApiItemResponse<Optional<Transaction>> getTransactionById(UUID transactionId) {
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        if (transaction.isPresent()) {
            return apiResponseBuilder.buildResponse(transaction, HttpStatus.OK, "Transaction found");
        } else {
            throw new AppException(ErrorCode.TRANSACTION_NOT_FOUND);
        }
    }

    @Override
    public ApiItemResponse<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return apiResponseBuilder.buildResponse(transactions, HttpStatus.OK, "All transactions retrieved");
    }

    @Override
    public ApiItemResponse<Transaction> updateTransaction(UUID transactionId, TransactionDtoRequest transactionDtoRequest) {
        if (!transactionRepository.existsById(transactionId)) {
            throw new AppException(ErrorCode.TRANSACTION_NOT_FOUND);
        }

        // Find the existing transaction
        Transaction existingTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new AppException(ErrorCode.TRANSACTION_NOT_FOUND));

        // Update fields
        existingTransaction.setAmount(transactionDtoRequest.amount());
        existingTransaction.setTransactionType(transactionDtoRequest.transactionType());
        existingTransaction.setDescription(transactionDtoRequest.description());

        // Save the updated transaction
        Transaction updatedTransaction = transactionRepository.save(existingTransaction);
        return apiResponseBuilder.buildResponse(updatedTransaction, HttpStatus.OK, "Transaction updated successfully");
    }

    @Override
    public ApiItemResponse<Void> deleteTransaction(UUID transactionId) {
        if (!transactionRepository.existsById(transactionId)) {
            throw new AppException(ErrorCode.TRANSACTION_NOT_FOUND);
        }
        transactionRepository.deleteById(transactionId);
        return apiResponseBuilder.buildResponse(null, HttpStatus.OK, "Transaction deleted successfully");
    }
}
