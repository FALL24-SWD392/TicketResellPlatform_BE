package com.swd392.ticket_resell_be.services.impls;


import com.swd392.ticket_resell_be.dtos.requests.FeedbackDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.entities.Feedback;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.FeedbackRepository;
import com.swd392.ticket_resell_be.repositories.OrderRepository;
import com.swd392.ticket_resell_be.services.FeedbackService;
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
public class FeedbackServiceImplement implements FeedbackService {
    FeedbackRepository feedbackRepository;
    OrderRepository orderRepository;

    ApiResponseBuilder apiResponseBuilder;
    ModelMapper modelMapper;


    @Override
    public ApiItemResponse<Feedback> createFeedback(FeedbackDtoRequest feedbackDtoRequest) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);
        try {
            Feedback feedback = modelMapper.map(feedbackDtoRequest, Feedback.class);
            UUID orderId = feedbackDtoRequest.order_id();
            boolean check = orderRepository.findByIdAndStatus(orderId, Categorize.APPROVED);
            if (!check) {
                throw new AppException(ErrorCode.USER_HAVE_NOT_BUY_YET);
            }
            feedback.setStatus(Categorize.PENDING);
            return apiResponseBuilder.buildResponse(
                    feedbackRepository.save(feedback),
                    HttpStatus.CREATED,
                    null
            );
        } catch (AppException e) {
            return apiResponseBuilder.buildResponse(
                    null,
                    HttpStatus.CONFLICT,
                    "Error at FeedbackServiceImplement"
            );
        }
    }

    @Override
    public ApiItemResponse<Feedback> updateFeedback(UUID id, FeedbackDtoRequest feedbackDtoRequest) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);

        Feedback existingFeedback = feedbackRepository.findFeedbackByIdIs(id);
        if (existingFeedback.getStatus() == Categorize.REMOVED) {
            throw new AppException(ErrorCode.FEEDBACK_NOT_FOUND);
        }
        modelMapper.map(feedbackDtoRequest, existingFeedback);
        existingFeedback.setStatus(Categorize.PENDING);
        return apiResponseBuilder.buildResponse(
                feedbackRepository.save(existingFeedback),
                HttpStatus.OK,
                null
        );
    }

    @Override
    public ApiItemResponse<Feedback> removeFeedback(UUID id) {
        Feedback feedback = feedbackRepository.findFeedbackByIdIs(id);
        feedback.setStatus(Categorize.REMOVED);
        return apiResponseBuilder.buildResponse(
                feedbackRepository.save(feedback),
                HttpStatus.OK,
                null
        );
    }

    @Override
    public ApiListResponse<Feedback> findFeedbackByOrderId(UUID id, Categorize status) {
        List<UUID> uuidList = feedbackRepository.findAllByOrderIdAndStatus(id, status)
                .stream()
                .map(Feedback::getId)
                .toList();
        List<Feedback> feedbackList = feedbackRepository.findAllById(uuidList);
        return apiResponseBuilder.buildResponse(
                feedbackList,
                0,
                0,
                0,
                0,
                HttpStatus.OK,
                null
        );
    }
}
