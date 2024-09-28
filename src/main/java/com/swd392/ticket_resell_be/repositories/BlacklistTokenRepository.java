package com.swd392.ticket_resell_be.repositories;

import com.swd392.ticket_resell_be.entities.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken, UUID> {
}