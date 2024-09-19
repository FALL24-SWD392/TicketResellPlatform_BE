package com.swd392.ticket_resell_be.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import com.swd392.ticket_resell_be.entities.User; // Ensure to import the User entity
import com.swd392.ticket_resell_be.entities.Package; // Ensure to import the Package entity

public record SubscriptionDtoRequest(
        @NotEmpty(message = "User cannot be null") User user, // Use User entity
        @NotEmpty(message = "Package cannot be null") Package aPackage // Use Package entity
) {}
