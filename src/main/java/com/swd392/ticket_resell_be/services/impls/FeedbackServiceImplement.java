package com.swd392.ticket_resell_be.services.impls;


import com.swd392.ticket_resell_be.dtos.requests.FeedbackDtoRequest;
import com.swd392.ticket_resell_be.dtos.requests.PageDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.FeedbackDtoResponse;
import com.swd392.ticket_resell_be.entities.Feedback;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.FeedbackRepository;
import com.swd392.ticket_resell_be.repositories.OrderRepository;
import com.swd392.ticket_resell_be.services.FeedbackService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import com.swd392.ticket_resell_be.utils.PagingUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
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
    PagingUtil pagingUtil;


    @Override
    public ApiItemResponse<Feedback> createFeedback(FeedbackDtoRequest feedbackDtoRequest) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);

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
    public ApiListResponse<FeedbackDtoResponse> findFeedbackByOrderId(UUID id, Categorize status, PageDtoRequest pageDtoRequest) {
        Page<Feedback> feedbacks = feedbackRepository.findAllByOrderIdAndStatus(id, status, pagingUtil.getPageable(pageDtoRequest));
        return apiResponseBuilder.buildResponse(
                parseToFeedBack(feedbacks),
                feedbacks.getSize(),
                feedbacks.getNumber(),
                feedbacks.getTotalElements(),
                feedbacks.getTotalPages(),
                HttpStatus.OK,
                null
        );
    }

    private List<FeedbackDtoResponse> parseToFeedBack(Page<Feedback> feedbacks) {
        return feedbacks.getContent().stream()
                .map(feedback -> new FeedbackDtoResponse(
                        feedback.getId(),
                        feedback.getBuyer().getId(),
                        feedback.getOrder().getId(),
                        feedback.getDescription(),
                        feedback.getRating(),
                        feedback.getStatus(),
                        feedback.getCreatedAt(),
                        feedback.getUpdatedAt()))
                .toList();
    }
}
