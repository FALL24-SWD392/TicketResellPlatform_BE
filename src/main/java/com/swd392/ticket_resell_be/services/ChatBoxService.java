package com.swd392.ticket_resell_be.services;


import com.swd392.ticket_resell_be.entities.ChatBox;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public interface ChatBoxService {
    Optional<String> getChatRoomId(UUID senderId, UUID recipientId, boolean createNewRoomIfNotExists);

    String createChatId(UUID senderId, UUID recipientId);

    ChatBox findById(String id);
}
