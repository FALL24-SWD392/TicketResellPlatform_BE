package com.swd392.ticket_resell_be.services.impls;


import com.swd392.ticket_resell_be.dtos.requests.FeedbackDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.FeedbackDtoResponse;
import com.swd392.ticket_resell_be.entities.ChatBox;
import com.swd392.ticket_resell_be.entities.Feedback;
import com.swd392.ticket_resell_be.entities.Order;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.FeedbackRepository;
import com.swd392.ticket_resell_be.services.ChatBoxService;
import com.swd392.ticket_resell_be.services.FeedbackService;
import com.swd392.ticket_resell_be.services.OrderService;
import com.swd392.ticket_resell_be.services.UserService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import com.swd392.ticket_resell_be.utils.PagingUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FeedbackServiceImplement implements FeedbackService {
    FeedbackRepository feedbackRepository;
    UserService userService;
    OrderService orderService;
    ChatBoxService chatBoxService;
    ApiResponseBuilder apiResponseBuilder;
    PagingUtil pagingUtil;


    @Override
    public ApiItemResponse<FeedbackDtoResponse> createFeedback(FeedbackDtoRequest feedbackDtoRequest) {
        Order selectedOrder = orderService.findById(feedbackDtoRequest.order_id());
        User buyer = userService.findById(feedbackDtoRequest.buyer_id());

        if(feedbackRepository.findByOrderAndBuyer(selectedOrder, buyer) != null)
            throw new AppException(ErrorCode.USER_ALREADY_FEEDBACK_THIS_ORDER);

        Feedback feedback = new Feedback();
        mapperHandmade(feedback, feedbackDtoRequest);
        feedback.setStatus(Categorize.COMPLETED);
        feedbackRepository.save(feedback);

        plusReputation(feedback.getOrder().getChatBox().getSender());
        plusReputation(feedback.getOrder().getChatBox().getRecipient());

        User ratedUser = userService
                .findById(orderService
                        .findById(feedbackDtoRequest.order_id())
                        .getChatBox().getRecipient().getId());
        float currentRate = ratedUser.getRating();

        List<ChatBox> chatMessages = chatBoxService.findChatBoxesByRecipient(ratedUser);

        List<Order> orders = new ArrayList<>();

        chatMessages.forEach(chat -> {
            Order order = orderService.findByChatBox(chat);
            if (order != null)
                orders.add(order);
        });

        int numberOfOrders = orders.size();

        userService.updateRating(
                (currentRate * (numberOfOrders - 1) + feedbackDtoRequest.rating()) / numberOfOrders,
                ratedUser.getUsername());

        return apiResponseBuilder.buildResponse(
                parseToFeedbackDtoResponse(feedback),
                HttpStatus.CREATED
        );
    }

    private void plusReputation(User seller) {
        int repu = seller.getReputation();
        if (repu <= 95)
            repu += 5;
        else if (repu < 100)
            repu += 1;
        userService.updateReputation(repu, seller.getUsername());
    }

    private void mapperHandmade(Feedback feedback, FeedbackDtoRequest feedbackDtoRequest) {
        boolean check_1 = orderService.findById(feedbackDtoRequest.order_id()).getChatBox().getSender() == userService.findById(feedbackDtoRequest.buyer_id());
        boolean check_2 = orderService.findById(feedbackDtoRequest.order_id()).getChatBox().getRecipient() == userService.findById(feedbackDtoRequest.buyer_id());
        if (check_1 || check_2) {
            feedback.setBuyer(userService.findById(feedbackDtoRequest.buyer_id()));
            feedback.setOrder(orderService.findById(feedbackDtoRequest.order_id()));
            feedback.setDescription(feedbackDtoRequest.description());
            feedback.setRating(feedbackDtoRequest.rating());
            feedback.setStatus(feedbackDtoRequest.status());
        }
    }

    private FeedbackDtoResponse parseToFeedbackDtoResponse(Feedback feedback) {
        FeedbackDtoResponse feedbackDtoResponse = new FeedbackDtoResponse();
        feedbackDtoResponse.setId(feedback.getId());
        feedbackDtoResponse.setBuyerId(feedback.getBuyer().getId());
        feedbackDtoResponse.setOrderId(feedback.getOrder().getId());
        feedbackDtoResponse.setDescription(feedback.getDescription());
        feedbackDtoResponse.setRating(feedback.getRating());
        feedbackDtoResponse.setStatus(feedback.getStatus());
        feedbackDtoResponse.setCreatedAt(feedback.getCreatedAt());
        feedbackDtoResponse.setUpdatedAt(feedback.getUpdatedAt());

        return feedbackDtoResponse;
    }

    private List<FeedbackDtoResponse> parseToFeedbackDtoResponses(Page<Feedback> feedbacks) {
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

    @Override
    public ApiItemResponse<FeedbackDtoResponse> updateFeedback(UUID id, FeedbackDtoRequest feedbackDtoRequest) {
        Feedback existingFeedback = feedbackRepository.findFeedbackByIdIs(id);
        if (existingFeedback.getStatus() == Categorize.REMOVED)
            throw new AppException(ErrorCode.FEEDBACK_NOT_FOUND);

        User ratedUser = userService
                .findById(orderService
                        .findById(feedbackDtoRequest.order_id())
                        .getChatBox().getRecipient().getId());
        float currentRate = ratedUser.getRating();

        List<ChatBox> chatMessages = chatBoxService.findChatBoxesByRecipient(ratedUser);

        List<Order> orders = new ArrayList<>();

        chatMessages.forEach(chat -> {
            Order order = orderService.findByChatBox(chat);
            if (order != null)
                orders.add(order);
        });

        int numberOfOrders = orders.size();

        userService.updateRating(
                (currentRate * numberOfOrders - existingFeedback.getRating() + feedbackDtoRequest.rating())
                        / numberOfOrders,
                ratedUser.getUsername());

        mapperHandmade(existingFeedback, feedbackDtoRequest);
        existingFeedback.setId(id);
        existingFeedback.setStatus(Categorize.PENDING);
        feedbackRepository.save(existingFeedback);
        return apiResponseBuilder.buildResponse(
                parseToFeedbackDtoResponse(existingFeedback),
                HttpStatus.OK
        );
    }

    @Override
    public ApiItemResponse<FeedbackDtoResponse> removeFeedback(UUID id) {
        Feedback feedback = feedbackRepository.findFeedbackByIdIs(id);
        feedback.setStatus(Categorize.REMOVED);
        feedbackRepository.save(feedback);
        return apiResponseBuilder.buildResponse(
                parseToFeedbackDtoResponse(feedback),
                HttpStatus.OK
        );
    }

    @Override
    public ApiListResponse<FeedbackDtoResponse> findFeedbackByOrderId(UUID id, Categorize status, int page, int size, Sort.Direction direction, String... properties) {
        Page<Feedback> feedbacks = feedbackRepository.findByOrderChatBoxRecipient(id, pagingUtil
                .getPageable(Feedback.class, page, size, direction, properties));
        if (feedbacks.isEmpty())
            throw new AppException(ErrorCode.FEEDBACK_NOT_FOUND);
        else
            return apiResponseBuilder.buildResponse(
                    parseToFeedbackDtoResponses(feedbacks),
                    feedbacks.getSize(),
                    feedbacks.getNumber() + 1,
                    feedbacks.getTotalElements(),
                    feedbacks.getTotalPages(),
                    HttpStatus.OK,
                    null
            );
    }
}
