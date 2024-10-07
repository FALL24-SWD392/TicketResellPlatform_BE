package com.swd392.ticket_resell_be.controllers;

import com.swd392.ticket_resell_be.dtos.requests.ChatBoxDtoRequest;
import com.swd392.ticket_resell_be.dtos.requests.ChatMessageDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.entities.ChatBox;
import com.swd392.ticket_resell_be.entities.ChatMessage;
import com.swd392.ticket_resell_be.services.ChatBoxService;
import com.swd392.ticket_resell_be.services.ChatMessageService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/chats")
@Tag(name = "Chat APIs")
public class ChatController {
    ChatBoxService chatBoxService;
    ChatMessageService chatMessageService;

    @PostMapping("/chat-boxes")
    public ResponseEntity<ApiItemResponse<ChatBox>> createChatBox(
            @RequestBody @Valid ChatBoxDtoRequest chatBoxDtoRequest) {
        return ResponseEntity.ok(chatBoxService.createChatBox(chatBoxDtoRequest));
    }

    @GetMapping("/chat-boxes/view-all-chat-box")
    public ResponseEntity<ApiListResponse<ChatBox>> viewAllChatBox() {
        return ResponseEntity.ok(chatBoxService.viewAllChatBox());
    }

    @GetMapping("/chat-boxes/view-all-chat-box-by-user-id")
    public ResponseEntity<ApiListResponse<ChatBox>> viewAllChatBoxByUserId(
            @RequestParam @Valid UUID userId) {
        return ResponseEntity.ok(chatBoxService.viewAllChatBoxByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<ApiItemResponse<ChatMessage>> createChatMessage(
            @RequestBody @Valid ChatMessageDtoRequest chatMessageDtoRequest) {
        return ResponseEntity.ok(chatMessageService.createChatMessage(chatMessageDtoRequest));
    }


    @GetMapping("/view-all-chat-message")
    public ResponseEntity<ApiListResponse<ChatMessage>> viewAllChatMessage() {
        return ResponseEntity.ok(chatMessageService.viewAllChatMessage());
    }

    @GetMapping("/view-all-chat-mess-by-box")
    public ResponseEntity<ApiListResponse<ChatMessage>> viewAllChatMessageByChatBox(
            @RequestBody @Valid UUID chatBoxId) {
        return ResponseEntity.ok(chatMessageService.viewAllChatMessageByChatBox(chatBoxId));
    }
}
