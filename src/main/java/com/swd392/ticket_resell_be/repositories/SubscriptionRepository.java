package com.swd392.ticket_resell_be.repositories;

import com.swd392.ticket_resell_be.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    @Query("SELECT p FROM subscriptions p WHERE p.id = :packageId")
    Optional<Subscription> findById(UUID packageId);
    @Query("SELECT p FROM subscriptions p")
    List<Subscription> findAll();
}
