package com.swd392.ticket_resell_be.controllers;

import com.swd392.ticket_resell_be.dtos.requests.FeedbackDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.entities.Feedback;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.services.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/feedback")
public class FeedbackController {
    FeedbackService feedbackService;

    @PostMapping("/create")
    public ResponseEntity<ApiItemResponse<Feedback>> createFeedback(
            @RequestBody @Valid FeedbackDtoRequest feedbackDtoRequest) {
        return ResponseEntity.ok(feedbackService.createFeedback(feedbackDtoRequest));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiItemResponse<Feedback>> updateFeedback(
            @RequestParam UUID id,
            @RequestBody @Valid FeedbackDtoRequest feedbackDtoRequest) {
        return ResponseEntity.ok(feedbackService.updateFeedback(id, feedbackDtoRequest));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiItemResponse<Feedback>> removeFeedback(
            @RequestParam @Valid UUID id) {
        return ResponseEntity.ok(feedbackService.removeFeedback(id));
    }

    @GetMapping("/view-by-user-id")
    public ResponseEntity<ApiListResponse<Feedback>> getFeedbackByUserId(
            @RequestParam @Valid UUID id, Categorize status) {
        return ResponseEntity.ok(feedbackService.findFeedbackByOrderId(id, status));
    }
}
