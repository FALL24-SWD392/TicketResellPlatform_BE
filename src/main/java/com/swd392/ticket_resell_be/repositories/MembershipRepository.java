package com.swd392.ticket_resell_be.repositories;

import com.swd392.ticket_resell_be.entities.Membership;
import com.swd392.ticket_resell_be.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, UUID> {
    @Modifying
    @Transactional
    @Query("UPDATE Membership m SET m.active = false WHERE m.user = :user AND m.active = true")
    int deactivateActiveMemberships(@Param("user") User user);
}