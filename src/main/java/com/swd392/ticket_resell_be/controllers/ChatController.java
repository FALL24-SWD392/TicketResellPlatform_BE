package com.swd392.ticket_resell_be.controllers;


import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.entities.ChatMessage;
import com.swd392.ticket_resell_be.entities.ChatNotification;
import com.swd392.ticket_resell_be.services.ChatMessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/chats")
@Tag(name = "Chat APIs")
public class ChatController {
    SimpMessagingTemplate simpMessagingTemplate;
    ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        simpMessagingTemplate.convertAndSendToUser(
                (chatMessage.getRecipient().getId()).toString(),
                "/queue/messages",
                ChatNotification.builder()
                        .id(savedMsg.getId())
                        .sender(savedMsg.getSender())
                        .recipient(savedMsg.getRecipient())
                        .content(savedMsg.getMessage())
                        .build()
        );
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<ApiListResponse<ChatMessage>> findChatMessages(
            @PathVariable("senderId") UUID senderId,
            @PathVariable("recipientId") UUID recipientId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(defaultValue = "id") String... properties
    ) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId, page - 1, size, direction, properties));
    }
}
