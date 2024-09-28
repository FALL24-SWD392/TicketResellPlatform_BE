package com.swd392.ticket_resell_be.services;

import com.swd392.ticket_resell_be.dtos.requests.ChatMessageDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.entities.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ChatMessageService {
    ApiItemResponse<ChatMessage> createChatMessage(ChatMessageDtoRequest chatMessageDtoRequest);

    ApiListResponse<ChatMessage> viewAllChatMessage();

    ApiListResponse<ChatMessage> viewAllChatMessageByChatBox(UUID chatBoxId);
}
