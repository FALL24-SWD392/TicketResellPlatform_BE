package com.swd392.ticket_resell_be.repositories;

import com.swd392.ticket_resell_be.entities.Token;
import com.swd392.ticket_resell_be.enums.Categorize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {
    boolean existsByIdAndStatus(UUID id, Categorize status);

    void removeByExpAtBefore(Date expAt);
}