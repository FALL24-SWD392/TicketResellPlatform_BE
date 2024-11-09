package com.swd392.ticket_resell_be.repositories;

import com.swd392.ticket_resell_be.entities.ChatBox;
import com.swd392.ticket_resell_be.entities.Order;
import com.swd392.ticket_resell_be.enums.Categorize;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByChatBox(ChatBox chatBox);

    Boolean findByIdAndStatus(UUID id, Categorize status);

    Order findById(UUID id);

    @NotNull
    Page<Order> findAll(@NotNull Pageable page);

    @Query("SELECT o FROM Order o " +
            "JOIN o.chatBox cb " +
            "JOIN cb.recipient u " +
            "WHERE u.id = :userId ")
    Page<Order> findByOrderChatBoxUserId(@Param("userId") UUID userId, Pageable pageable);
}
