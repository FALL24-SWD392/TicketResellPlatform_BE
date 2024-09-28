package com.swd392.ticket_resell_be.services;


import com.swd392.ticket_resell_be.dtos.requests.ChatBoxDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.entities.ChatBox;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface ChatBoxService {
    ApiItemResponse<ChatBox> createChatBox(ChatBoxDtoRequest chatBoxDtoRequest);

    ApiListResponse<ChatBox> viewAllChatBox();

    ApiListResponse<ChatBox> viewAllChatBoxByUserId(UUID userId);
}
