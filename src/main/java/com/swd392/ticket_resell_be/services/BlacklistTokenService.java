package com.swd392.ticket_resell_be.services;

import com.swd392.ticket_resell_be.entities.BlacklistToken;

import java.util.UUID;

public interface BlacklistTokenService {
    void save(BlacklistToken blacklistToken);

    boolean existsById(UUID id);
}
