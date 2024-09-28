package com.swd392.ticket_resell_be.repositories;

import com.swd392.ticket_resell_be.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    Collection<ChatMessage> findAllByChatBoxId(UUID chatBoxId);
}