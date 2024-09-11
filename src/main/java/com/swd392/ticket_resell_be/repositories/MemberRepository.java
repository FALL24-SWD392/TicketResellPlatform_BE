package com.swd392.ticket_resell_be.repositories;

import com.swd392.ticket_resell_be.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByUsername(String username);
}