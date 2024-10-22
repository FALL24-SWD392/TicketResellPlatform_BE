package com.swd392.ticket_resell_be.controllers;

import com.swd392.ticket_resell_be.dtos.requests.PageDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.TransactionDtoResponse;
import com.swd392.ticket_resell_be.services.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiListResponse<TransactionDtoResponse>> getAllTransactions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageDtoRequest pageDtoRequest = new PageDtoRequest(size, page - 1);
        return ResponseEntity.ok(transactionService.getAllTransactions(pageDtoRequest));
    }

    @GetMapping("/user")
    public ResponseEntity<ApiListResponse<TransactionDtoResponse>> getAllTransactionsByUsername(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        PageDtoRequest pageDtoRequest = new PageDtoRequest(size, page - 1 );
        return ResponseEntity.ok(transactionService.getAllTransactionsByUsername(pageDtoRequest));
    }

}
