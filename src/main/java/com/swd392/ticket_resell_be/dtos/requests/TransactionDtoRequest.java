package com.swd392.ticket_resell_be.dtos.requests;
import com.swd392.ticket_resell_be.entities.Package;
import com.swd392.ticket_resell_be.entities.Transaction;

import com.swd392.ticket_resell_be.entities.User;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link Transaction}
 */
public record TransactionDtoRequest(
        @NotEmpty(message = "Amount cannot be null") BigDecimal amount,
        @Size(max = 50, message = "Transaction type cannot exceed 50 characters") String transactionType,
        String description,
        Package aPackage,
        User user
) implements Serializable {
}

