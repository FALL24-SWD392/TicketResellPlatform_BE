package com.swd392.ticket_resell_be.repositories;

import com.swd392.ticket_resell_be.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    List<Subscription> findByEndDateBeforeAndIsActive(LocalDate endDate, boolean isActive);
}
