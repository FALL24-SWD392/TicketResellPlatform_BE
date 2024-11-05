package com.swd392.ticket_resell_be.repositories;

import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.Categorize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Page<User> findByUsernameContainsIgnoreCase(String username, Pageable pageable);

    Optional<User> findByUsernameAndStatus(String username, Categorize status);
    Optional<User> findByUsernameAndStatusIn(String username, List<Categorize> statuses);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndTypeRegisterAndStatus(String email, Categorize typeRegister, Categorize status);
}