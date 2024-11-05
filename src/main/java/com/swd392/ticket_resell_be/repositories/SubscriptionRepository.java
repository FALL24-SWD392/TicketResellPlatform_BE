package com.swd392.ticket_resell_be.repositories;

import com.swd392.ticket_resell_be.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    Optional<Subscription> findByName(String name);
}
