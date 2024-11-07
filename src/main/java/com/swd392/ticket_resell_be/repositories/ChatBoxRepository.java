package com.swd392.ticket_resell_be.repositories;


import com.swd392.ticket_resell_be.entities.ChatBox;
import com.swd392.ticket_resell_be.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ChatBoxRepository extends JpaRepository<ChatBox, UUID> {
    Optional<ChatBox> findBySenderIdAndRecipientId(UUID senderId, UUID recipientId);

    ChatBox findById(String id);

    List<ChatBox> findByRecipient(User user);
}
