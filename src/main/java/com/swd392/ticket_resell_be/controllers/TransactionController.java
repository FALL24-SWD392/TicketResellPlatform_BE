package com.swd392.ticket_resell_be.controllers;

import com.swd392.ticket_resell_be.dtos.requests.PageDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.TransactionDtoResponse;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.services.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction APIs")
public class TransactionController {

    TransactionService transactionService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<ApiListResponse<TransactionDtoResponse>>
    getAllTransactions(
            @RequestParam(defaultValue = "") String description,
            @RequestParam(defaultValue = "")Categorize status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(defaultValue = "id") String... properties) {
        return ResponseEntity.ok(transactionService.getAllTransactions(description, status, page - 1, size, direction, properties));
    }

    @GetMapping("/user")
    public ResponseEntity<ApiListResponse<TransactionDtoResponse>> getAllTransactionsByUsername(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(defaultValue = "id") String... properties) {
        return ResponseEntity.ok(transactionService.getAllTransactionsByUser(page - 1, size, direction, properties));
    }

}
