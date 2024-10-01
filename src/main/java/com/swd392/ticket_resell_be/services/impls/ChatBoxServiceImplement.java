package com.swd392.ticket_resell_be.services.impls;


import com.swd392.ticket_resell_be.dtos.requests.ChatBoxDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.entities.ChatBox;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.repositories.ChatBoxRepository;
import com.swd392.ticket_resell_be.repositories.UserRepository;
import com.swd392.ticket_resell_be.services.ChatBoxService;
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
public class ChatBoxServiceImplement implements ChatBoxService {
    ChatBoxRepository chatBoxRepository;
    ApiResponseBuilder apiResponseBuilder;

    UserRepository userRepository;

    ModelMapper modelMapper;

    @Override
    public ApiItemResponse<ChatBox> createChatBox(ChatBoxDtoRequest chatBoxDtoRequest) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);
        ChatBox chatBox = modelMapper.map(chatBoxDtoRequest, ChatBox.class);
        return apiResponseBuilder.buildResponse(
                chatBoxRepository.save(chatBox),
                HttpStatus.CREATED,
                null
        );
    }

    @Override
    public ApiListResponse<ChatBox> viewAllChatBox() {
        List<ChatBox> chatBoxList = chatBoxRepository.findAll();
        return apiResponseBuilder.buildResponse(
                chatBoxList,
                0,
                0,
                0,
                0,
                HttpStatus.OK,
                null

        );
    }

    @Override
    public ApiListResponse<ChatBox> viewAllChatBoxByUserId(UUID userId) {
        User user = userRepository.findById(userId).orElse(null);
        List<UUID> uuidList = chatBoxRepository.findChatBoxByBuyerOrSeller(user, user)
                .stream()
                .map(ChatBox::getId)
                .toList();
        List<ChatBox> chatBoxList = chatBoxRepository.findAllById(uuidList);
        return apiResponseBuilder.buildResponse(
                chatBoxList,
                0,
                0,
                0,
                0,
                HttpStatus.OK,
                null
        );
    }
}
