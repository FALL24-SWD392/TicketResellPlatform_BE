package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.requests.PageDtoRequest;
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
import com.swd392.ticket_resell_be.utils.PagingUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TransactionServiceImplement implements TransactionService {

    TransactionRepository transactionRepository;
    ApiResponseBuilder apiResponseBuilder;
    UserService userService;
    PagingUtil pagingUtil;

    @Override
    public ApiItemResponse<Transaction> savePendingTransaction(Subscription subscription, User user, String orderCode) {
        if (subscription == null || user == null || orderCode == null) {
            throw new AppException(ErrorCode.TRANSACTION_DATA_INVALID);
        }
        Transaction transaction = new Transaction();
        transaction.setOrderCode(orderCode);
        transaction.setSubscription(subscription);
        transaction.setSeller(user);
        transaction.setDescription("Thanh toán cho gói: " + subscription.getName());
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
    public ApiListResponse<TransactionDtoResponse> getAllTransactions(PageDtoRequest pageDtoRequest) {
        Page<Transaction> transactions = transactionRepository.findAll(pagingUtil.getPageable(pageDtoRequest));
        if (transactions.isEmpty()) {
            throw new AppException(ErrorCode.TICKET_NOT_FOUND);
        }
        return apiResponseBuilder.buildResponse(
                mapToDto(transactions),
                transactions.getSize(),
                transactions.getNumber(),
                transactions.getTotalElements(),
                transactions.getTotalPages(),
                HttpStatus.OK,
                null
        );
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
    public ApiListResponse<TransactionDtoResponse> getAllTransactionsByUsername(PageDtoRequest pageDtoRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        String username = authentication.getName();
        User user = userService.getUserByName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Page<Transaction> transactions = transactionRepository.findBySeller(user, pagingUtil.getPageable(pageDtoRequest));
        List<TransactionDtoResponse> transactionDtoResponses = mapToDto(transactions);
        return apiResponseBuilder.buildResponse(
                transactionDtoResponses,
                transactions.getSize(),
                transactions.getNumber(),
                transactions.getTotalElements(),
                transactions.getTotalPages(),
                HttpStatus.OK
        );
    }



    private List<TransactionDtoResponse> mapToDto(Page<Transaction> transactions) {
        return transactions.getContent().stream()
                .map(transaction -> {
                    TransactionDtoResponse dto = new TransactionDtoResponse();
                    dto.setId(transaction.getId());
                    dto.setOrderCode(transaction.getOrderCode());
                    dto.setUserName(transaction.getSeller().getUsername());
                    dto.setStatus(transaction.getStatus());
                    dto.setCreatedAt(transaction.getCreatedAt());
                    dto.setUpdatedAt(transaction.getUpdatedAt());
                    dto.setDescription(transaction.getDescription());
                    return dto;
                })
                .toList();
    }

}
