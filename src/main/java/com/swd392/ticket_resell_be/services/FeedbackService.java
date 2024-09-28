package com.swd392.ticket_resell_be.services;


import com.swd392.ticket_resell_be.dtos.requests.FeedbackDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.entities.Feedback;
import com.swd392.ticket_resell_be.enums.Categorize;

import java.util.UUID;


public interface FeedbackService {
    ApiItemResponse<Feedback> createFeedback(FeedbackDtoRequest feedbackDtoRequest);

    ApiItemResponse<Feedback> updateFeedback(UUID id, FeedbackDtoRequest feedbackDtoRequest);

    ApiItemResponse<Feedback> removeFeedback(UUID id);

    ApiListResponse<Feedback> findFeedbackByOrderId(UUID id, Categorize status);
}
