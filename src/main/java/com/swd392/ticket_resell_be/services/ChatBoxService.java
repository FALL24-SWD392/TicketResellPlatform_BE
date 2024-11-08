package com.swd392.ticket_resell_be.services;


import com.swd392.ticket_resell_be.entities.ChatBox;
import com.swd392.ticket_resell_be.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ChatBoxService {
    ChatBox findById(String id);

    List<ChatBox> findChatBoxesByRecipient(User user);

    ChatBox createChatBox(String chatBoxId, UUID senderId, UUID recipientId, UUID ticketId);
}
