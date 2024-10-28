package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.TransactionDtoResponse;
import com.swd392.ticket_resell_be.entities.Subscription;
import com.swd392.ticket_resell_be.entities.Transaction;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.TransactionRepository;
import com.swd392.ticket_resell_be.services.TransactionService;
import com.swd392.ticket_resell_be.services.UserService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TransactionServiceImplement implements TransactionService {

    TransactionRepository transactionRepository;
    ApiResponseBuilder apiResponseBuilder;
    UserService userService;

    @Override
    public ApiItemResponse<Transaction> savePendingTransaction(Subscription subscription, User user, String orderCode) {
        if (subscription == null || user == null || orderCode == null) {
            throw new AppException(ErrorCode.TRANSACTION_DATA_INVALID);
        }
        Transaction transaction = new Transaction();
        transaction.setOrderCode(orderCode);
        transaction.setSubscription(subscription);
        transaction.setSeller(user);
        transaction.setStatus(Categorize.PENDING);
        transaction.setCreatedAt(new Date());
        transaction.setUpdatedAt(new Date());
        try {
            transactionRepository.save(transaction);
        } catch (Exception e) {
            throw new AppException(ErrorCode.TRANSACTION_CREATION_FAILED);
        }
        return apiResponseBuilder.buildResponse(transaction, HttpStatus.CREATED, "Pending transaction saved successfully");
    }

    @Override
    public ApiItemResponse<Transaction> findTransactionByOrderId(String orderCode) throws AppException {
        Transaction transaction = transactionRepository.getTransactionByOrderCode(orderCode)
                .orElseThrow(() -> new AppException(ErrorCode.TRANSACTION_NOT_FOUND));
        return apiResponseBuilder.buildResponse(transaction, HttpStatus.OK, "Transaction retrieved successfully");
    }

    @Override
    public ApiListResponse<TransactionDtoResponse> getAllTransactions() {
        // Create pagination request
        List<Transaction> transactions = transactionRepository.findAll();
        List<TransactionDtoResponse> transactionDtoResponses = transactions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return apiResponseBuilder.buildResponse(transactionDtoResponses, 0, 0, 0, 0, HttpStatus.OK, "All transactions retrieved successfully");
    }

    @Override
    public ApiItemResponse<Transaction> updateTransactionStatus(UUID transactionId, Categorize status) throws AppException {
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

    @Override
    public ApiListResponse<TransactionDtoResponse> getAllTransactionsByUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get the username of the logged-in user
        User user = userService.getUserByName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<Transaction> transactions = transactionRepository.findBySeller(user);

        List<TransactionDtoResponse> transactionDtoResponses = transactions.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return apiResponseBuilder.buildResponse(transactionDtoResponses, 0, 0, 0, 0, HttpStatus.OK);
    }

    private TransactionDtoResponse mapToDto(Transaction transaction) {
        TransactionDtoResponse dto = new TransactionDtoResponse();
        dto.setId(transaction.getId());
        dto.setOrderCode(transaction.getOrderCode());
        dto.setUserName(transaction.getSeller().getUsername());
        dto.setStatus(transaction.getStatus());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setUpdatedAt(transaction.getUpdatedAt());
        dto.setDescription("Thanh toán cho gói"
                + " "
                + transaction.getSubscription().getName());
        return dto;
    }
}
