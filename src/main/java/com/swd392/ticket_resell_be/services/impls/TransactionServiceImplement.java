package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.requests.PageDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.entities.Subscription;
import com.swd392.ticket_resell_be.entities.Transaction;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.TransactionRepository;
import com.swd392.ticket_resell_be.services.TransactionService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import com.swd392.ticket_resell_be.utils.PagingUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
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
    PagingUtil pagingUtil;

    @Override
    public ApiItemResponse<Transaction> savePendingTransaction(Subscription subscription, User user, String orderId) {
        if (subscription == null || user == null || orderId == null) {
            throw new AppException(ErrorCode.TRANSACTION_DATA_INVALID);
        }
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setSubscription(subscription);
        transaction.setSeller(user);
        transaction.setStatus((Categorize.PENDING));
        transactionRepository.save(transaction);
        return apiResponseBuilder.buildResponse(transaction, HttpStatus.CREATED, "Pending transaction saved successfully");

    }

//    @Override
//    public ApiItemResponse<Transaction> findTransactionByOrderId(String orderCode) throws AppException {
//        Transaction transaction = transactionRepository.findTransactionByOrderId(orderCode)
//                .orElseThrow(() -> new AppException(ErrorCode.TRANSACTION_NOT_FOUND));
//        return  apiResponseBuilder.buildResponse(transaction, HttpStatus.OK, "Subscription created successfully");
//    }

    @Override
    public ApiListResponse<Transaction> getAllTransactions(int page, int size) {
        PageDtoRequest pageDtoRequest = new PageDtoRequest(size, page);
        Page<Transaction> transactionPage = transactionRepository.findAll(pagingUtil.getPageable(pageDtoRequest));
        List<Transaction> transactions = transactionPage.getContent();

        return apiResponseBuilder.buildResponse(transactions, transactionPage.getSize(),
                transactionPage.getNumber(), transactionPage.getTotalElements(),
                transactionPage.getTotalPages(), HttpStatus.OK, "All transaction retrieved");
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

}
