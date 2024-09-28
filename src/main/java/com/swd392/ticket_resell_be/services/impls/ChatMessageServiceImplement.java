package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.requests.ChatMessageDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.entities.ChatMessage;
import com.swd392.ticket_resell_be.repositories.ChatMessageRepository;
import com.swd392.ticket_resell_be.services.ChatMessageService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ChatMessageServiceImplement implements ChatMessageService {

    ChatMessageRepository chatMessageRepository;
    ApiResponseBuilder apiResponseBuilder;

    ModelMapper modelMapper;

    @Override
    public ApiItemResponse<ChatMessage> createChatMessage(ChatMessageDtoRequest chatMessageDtoRequest) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);
        ChatMessage chatMessage = modelMapper.map(chatMessageDtoRequest, ChatMessage.class);
        return apiResponseBuilder.buildResponse(
                chatMessageRepository.save(chatMessage),
                HttpStatus.CREATED,
                null
        );
    }

    @Override
    public ApiListResponse<ChatMessage> viewAllChatMessage() {
        List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
        return apiResponseBuilder.buildResponse(
                chatMessageList,
                0,
                0,
                0,
                0,
                HttpStatus.OK,
                null
        );
    }

    @Override
    public ApiListResponse<ChatMessage> viewAllChatMessageByChatBox(UUID chatBoxId) {
        List<UUID> uuidList = chatMessageRepository.findAllByChatBoxId(chatBoxId)
                .stream()
                .map(ChatMessage::getId)
                .toList();
        List<ChatMessage> chatMessageList = chatMessageRepository.findAllById(uuidList);
        return apiResponseBuilder.buildResponse(
                chatMessageList,
                0,
                0,
                0,
                0,
                HttpStatus.OK,
                null
        );
    }
}
