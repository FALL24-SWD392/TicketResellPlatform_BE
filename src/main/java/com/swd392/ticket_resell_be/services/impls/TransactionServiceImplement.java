package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.entities.Package;
import com.swd392.ticket_resell_be.entities.Transaction;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.enums.TransactionStatus;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.TransactionRepository;
import com.swd392.ticket_resell_be.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TransactionServiceImplement implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public void savePendingTransaction(Package pkg, User user, String orderId) {
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setAPackage(pkg);
        transaction.setUser(user);
        transaction.setAmount(pkg.getPrice());
        transaction.setTransactionTime(new Date());
        transaction.setDescription(orderId);
        transaction.setStatus(TransactionStatus.PENDING);
        transactionRepository.save(transaction);
    }

    @Override
    public Transaction getTransactionById(UUID transactionId) throws AppException {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new AppException(ErrorCode.TRANSACTION_NOT_FOUND));
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Optional<Transaction> findTransactionByDescription(String orderCode) throws AppException {
        return transactionRepository.findTransactionsByDescription(orderCode);  // No need to throw exception, return Optional
    }

    @Override
    public void updateTransactionStatus(UUID transactionId, TransactionStatus status) throws AppException {
        Transaction transaction = getTransactionById(transactionId);
        transaction.setStatus(status);
        transactionRepository.save(transaction);
    }
}
