package com.swd392.ticket_resell_be.services;


import com.swd392.ticket_resell_be.dtos.requests.FeedbackDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.FeedbackDtoResponse;
import com.swd392.ticket_resell_be.enums.Categorize;
import org.springframework.data.domain.Sort;

import java.util.UUID;


public interface FeedbackService {
    ApiItemResponse<FeedbackDtoResponse> createFeedback(FeedbackDtoRequest feedbackDtoRequest);

    ApiItemResponse<FeedbackDtoResponse> updateFeedback(UUID id, FeedbackDtoRequest feedbackDtoRequest);

    ApiItemResponse<FeedbackDtoResponse> removeFeedback(UUID id);

    ApiListResponse<FeedbackDtoResponse> findFeedbackByOrderId(UUID id, Categorize status, int page, int size, Sort.Direction direction, String... properties);
}
