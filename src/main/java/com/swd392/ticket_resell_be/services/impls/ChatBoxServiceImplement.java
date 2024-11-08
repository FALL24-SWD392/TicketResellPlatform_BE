package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.entities.ChatBox;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.repositories.ChatBoxRepository;
import com.swd392.ticket_resell_be.services.ChatBoxService;
import com.swd392.ticket_resell_be.services.TicketService;
import com.swd392.ticket_resell_be.services.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ChatBoxServiceImplement implements ChatBoxService {
    ChatBoxRepository chatBoxRepository;
    UserService userService;
    TicketService ticketService;

    public ChatBox findById(String id) {
        return chatBoxRepository.findById(id);
    }

    @Override
    public List<ChatBox> findChatBoxesByRecipient(User user) {
        return chatBoxRepository.findByRecipient(user);
    }

    @Override
    public ChatBox createChatBox(String chatBoxId, UUID senderId, UUID recipientId, UUID ticketId) {
        ChatBox chatBox = new ChatBox(chatBoxId, userService.findById(senderId), userService.findById(recipientId), ticketService.getTicketById(ticketId), new Date());
        return chatBoxRepository.save(chatBox);
    }


}
