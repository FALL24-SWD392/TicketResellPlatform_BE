package com.swd392.ticket_resell_be.controllers;

import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.entities.Transaction;
import com.swd392.ticket_resell_be.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<ApiListResponse<Transaction>> getAllTransactions(   @RequestParam(defaultValue = "0") int page,
                                                                              @RequestParam(defaultValue = "10") int size) {
        ApiListResponse<Transaction> response = transactionService.getAllTransactions(page,size);
        return ResponseEntity.ok(response);
    }
}
