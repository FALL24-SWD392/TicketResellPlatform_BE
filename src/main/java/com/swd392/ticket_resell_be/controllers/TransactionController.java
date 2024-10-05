package com.swd392.ticket_resell_be.controllers;

import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.TransactionDtoResponse;
import com.swd392.ticket_resell_be.entities.Transaction;
import com.swd392.ticket_resell_be.services.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    TransactionService transactionService;

    @GetMapping
    public ResponseEntity<ApiListResponse<TransactionDtoResponse>> getAllTransactions(@RequestParam(defaultValue = "0") int page,
                                                                                      @RequestParam(defaultValue = "10") int size) {
        ApiListResponse<TransactionDtoResponse> response = transactionService.getAllTransactions(page, size);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/user")
    public ApiListResponse<TransactionDtoResponse> getAllTransactionsByUsername(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return transactionService.getAllTransactionsByUsername( page, size);
    }

}
