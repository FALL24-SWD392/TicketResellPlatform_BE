package com.swd392.ticket_resell_be.controllers;

import com.swd392.ticket_resell_be.dtos.requests.TransactionDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.entities.Transaction;
import com.swd392.ticket_resell_be.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/transactions")
public class TransactionController {
    TransactionService transactionService;

    @PostMapping
    public ResponseEntity<ApiItemResponse<Transaction>> createTransaction(@RequestBody @Valid TransactionDtoRequest transactionDtoRequest) {
        ApiItemResponse<Transaction> response = transactionService.saveTransaction(transactionDtoRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiItemResponse<Optional<Transaction>>> getTransactionById(@PathVariable("id") UUID transactionId) {
        ApiItemResponse<Optional<Transaction>> response = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiItemResponse<List<Transaction>>> getAllTransactions() {
        ApiItemResponse<List<Transaction>> response = transactionService.getAllTransactions();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiItemResponse<Transaction>> updateTransaction(@PathVariable("id") UUID transactionId, @RequestBody @Valid TransactionDtoRequest transactionDtoRequest) {
        ApiItemResponse<Transaction> response = transactionService.updateTransaction(transactionId, transactionDtoRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiItemResponse<Void>> deleteTransaction(@PathVariable("id") UUID transactionId) {
        ApiItemResponse<Void> response = transactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok(response);
    }
}
