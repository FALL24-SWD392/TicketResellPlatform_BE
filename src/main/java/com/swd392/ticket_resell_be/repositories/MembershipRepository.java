package com.swd392.ticket_resell_be.repositories;

import com.swd392.ticket_resell_be.entities.Membership;
import com.swd392.ticket_resell_be.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, UUID> {
    Optional<Membership> findMembershipBySeller(User user);


}
