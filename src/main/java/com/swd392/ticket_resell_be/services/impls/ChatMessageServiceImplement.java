package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.entities.ChatMessage;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.ChatMessageRepository;
import com.swd392.ticket_resell_be.services.ChatBoxService;
import com.swd392.ticket_resell_be.services.ChatMessageService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import com.swd392.ticket_resell_be.utils.PagingUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ChatMessageServiceImplement implements ChatMessageService {
    ChatMessageRepository chatMessageRepository;
    ChatBoxService chatBoxService;
    PagingUtil pagingUtil;
    private final ApiResponseBuilder apiResponseBuilder;

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        var chatId = chatBoxService.getChatRoomId(
                chatMessage.getSender().getId(),
                chatMessage.getRecipient().getId(),
                true
        ).orElseThrow(() -> new AppException(ErrorCode.CHAT_BOX_DOES_NOT_EXIST));
        chatMessage.setChatId(chatId);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    private List<ChatMessage> parseToList(Page<ChatMessage> chatMessages) {
        return chatMessages.getContent().stream()
                .map(chatMessage -> new ChatMessage(
                        chatMessage.getId(),
                        chatMessage.getChatId(),
                        chatMessage.getSender(),
                        chatMessage.getRecipient(),
                        chatMessage.getMessage(),
                        chatMessage.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public ApiListResponse<ChatMessage> findChatMessages(UUID senderId, UUID recipientId, int page, int size, Sort.Direction direction, String... properties) {
        var chatId = chatBoxService.getChatRoomId(senderId, recipientId, false);
        Page<ChatMessage> chatMessages = chatMessageRepository.findByChatId(chatId.get(), pagingUtil
                .getPageable(ChatMessage.class, page, size, direction, properties));
        return apiResponseBuilder.buildResponse(
                parseToList(chatMessages),
                chatMessages.getSize(),
                chatMessages.getNumber() + 1,
                chatMessages.getTotalElements(),
                chatMessages.getTotalPages(),
                HttpStatus.OK,
                null
        );
    }
}
