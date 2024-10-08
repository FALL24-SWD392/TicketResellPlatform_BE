package com.swd392.ticket_resell_be.services;

import com.swd392.ticket_resell_be.entities.Token;
import com.swd392.ticket_resell_be.enums.Categorize;

import java.util.UUID;

public interface TokenService {
    void save(Token token);

    void inactive(UUID id);

    boolean existsByIdAndStatus(UUID id, Categorize status);
}
