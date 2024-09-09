package com.swd392.ticket_resell_be.dtos.requests;

import com.swd392.ticket_resell_be.entities.User;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
public record LoginDtoRequest(@NotEmpty String username, @NotEmpty @Length(min = 8) String password) implements Serializable {
}