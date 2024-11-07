package com.swd392.ticket_resell_be.controllers;

import com.swd392.ticket_resell_be.dtos.requests.FeedbackDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.FeedbackDtoResponse;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.services.FeedbackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/feedbacks")
@Tag(name = "Feedback APIs")
public class FeedbackController {
    FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<ApiItemResponse<FeedbackDtoResponse>> createFeedback(
            @RequestBody @Valid FeedbackDtoRequest feedbackDtoRequest) {
        return ResponseEntity.ok(feedbackService.createFeedback(feedbackDtoRequest));
    }

    @PutMapping
    public ResponseEntity<ApiItemResponse<FeedbackDtoResponse>> updateFeedback(
            @RequestParam UUID id,
            @RequestBody @Valid FeedbackDtoRequest feedbackDtoRequest) {
        return ResponseEntity.ok(feedbackService.updateFeedback(id, feedbackDtoRequest));
    }

    @DeleteMapping
    public ResponseEntity<ApiItemResponse<FeedbackDtoResponse>> removeFeedback(
            @RequestParam @Valid UUID id) {
        return ResponseEntity.ok(feedbackService.removeFeedback(id));
    }

    @GetMapping
    public ResponseEntity<ApiListResponse<FeedbackDtoResponse>> getFeedbackByUserId(
            @RequestParam @Valid UUID userId,
            @RequestParam(defaultValue = "APPROVED") Categorize status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(defaultValue = "id") String... properties) {
        return ResponseEntity.ok(feedbackService.findFeedbackByOrderId(userId, status, page - 1, size, direction, properties));
    }
}
