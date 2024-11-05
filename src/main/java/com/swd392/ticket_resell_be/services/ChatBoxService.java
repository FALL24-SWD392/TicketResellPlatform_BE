package com.swd392.ticket_resell_be.services;


import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public interface ChatBoxService {
    Optional<String> getChatRoomId(UUID senderId, UUID recipientId, boolean createNewRoomIfNotExists);

    String createChatId(UUID senderId, UUID recipientId);
}
