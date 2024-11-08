package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.entities.ChatBox;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.repositories.ChatBoxRepository;
import com.swd392.ticket_resell_be.services.ChatBoxService;
import com.swd392.ticket_resell_be.services.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ChatBoxServiceImplement implements ChatBoxService {
    ChatBoxRepository chatBoxRepository;
    UserService userService;

    @Override
    public Optional<String> getChatRoomId(UUID senderId, UUID recipientId, boolean createNewRoomIfNotExists) {
        return chatBoxRepository.findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatBox::getChatId)
                .or(() -> {
                    if (createNewRoomIfNotExists) {
                        var chatId = createChatId(senderId, recipientId);
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });
    }

    @Override
    public String createChatId(UUID senderId, UUID recipientId) {
        var chatId = String.format("%s_%s", senderId, recipientId);

        User sender = userService.findById(senderId);
        User recipient = userService.findById(recipientId);

        ChatBox senderRecipient = ChatBox.builder()
                .chatId(chatId)
                .sender(sender)
                .recipient(recipient)
                .build();

        ChatBox recipientSender = ChatBox.builder()
                .chatId(chatId)
                .sender(sender)
                .recipient(recipient)
                .build();

        chatBoxRepository.save(senderRecipient);
        chatBoxRepository.save(recipientSender);

        return chatId;
    }

    public ChatBox findById(String id) {
        return chatBoxRepository.findById(id);
    }

    @Override
    public List<ChatBox> findChatBoxesByRecipient(User user) {
        return chatBoxRepository.findByRecipient(user);
    }
}
