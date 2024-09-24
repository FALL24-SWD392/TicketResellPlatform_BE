package com.swd392.ticket_resell_be.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record ChangePasswordDtoRequest(
        @NotEmpty(message = "PASSWORD_EMPTY")
        String oldPassword,
        @NotEmpty(message = "PASSWORD_EMPTY")
        @Length(min = 8, message = "PASSWORD_LENGTH")
        String newPassword,
        @NotEmpty(message = "PASSWORD_EMPTY")
        @Length(min = 8, message = "PASSWORD_LENGTH")
        String confirmPassword
) {
}
