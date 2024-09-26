package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.entities.Subscription;
import com.swd392.ticket_resell_be.entities.Transaction;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.enums.TransactionStatus;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.TransactionRepository;
import com.swd392.ticket_resell_be.services.TransactionService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TransactionServiceImplement implements TransactionService {

    TransactionRepository transactionRepository;
    ApiResponseBuilder apiResponseBuilder;

    @Override
    public ApiItemResponse<Transaction> savePendingTransaction(Subscription subscription, User user, String orderId) {
        if (subscription == null || user == null || orderId == null) {
            throw new AppException(ErrorCode.TRANSACTION_DATA_INVALID);
        }
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setSubscription(subscription);
        transaction.setUser(user);
        transaction.setOrderId(orderId);
        transaction.setStatus(TransactionStatus.PENDING);
        transactionRepository.save(transaction);
        return apiResponseBuilder.buildResponse(transaction,HttpStatus.CREATED,"Pending transaction saved successfully");

    }

    @Override
    public ApiItemResponse<Transaction> findTransactionByOrderId(String orderCode) throws AppException {
        Transaction transaction = transactionRepository.findTransactionByOrderId(orderCode)
                .orElseThrow(() -> new AppException(ErrorCode.TRANSACTION_NOT_FOUND));
        return  apiResponseBuilder.buildResponse(transaction, HttpStatus.OK, "Subscription created successfully");
    }

    @Override
    public ApiListResponse<Transaction> getAllTransactions(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Transaction> transactionPage = transactionRepository.findAll(pageable);
        List<Transaction> transactions = transactionPage.getContent();
        return ApiListResponse.<Transaction>builder()
                .data(transactions)
                .size(transactionPage.getSize())
                .page(transactionPage.getNumber())
                .totalSize((int) transactionPage.getTotalElements())
                .totalPage(transactionPage.getTotalPages())
                .message("All subscriptions retrieved")
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ApiItemResponse<Transaction> updateTransactionStatus(UUID transactionId, TransactionStatus status) throws AppException {
        Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionId);
        if (optionalTransaction.isPresent()) {
            Transaction transaction = optionalTransaction.get(); // Get the transaction
            transaction.setStatus(status);
            Transaction updatedTransaction = transactionRepository.save(transaction);
            return apiResponseBuilder.buildResponse(updatedTransaction, HttpStatus.OK, "Transaction status updated successfully");
        } else {
            throw new AppException(ErrorCode.TRANSACTION_NOT_FOUND); // Define the appropriate error code
        }
    }


}
