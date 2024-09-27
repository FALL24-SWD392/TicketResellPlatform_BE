package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.requests.SubscriptionDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.entities.Subscription;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.SubscriptionRepository;
import com.swd392.ticket_resell_be.services.SubscriptionService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SubscriptionServiceImplement implements SubscriptionService {

    SubscriptionRepository subscriptionRepository;
    ApiResponseBuilder apiResponseBuilder;

    @Override
    public ApiItemResponse<Subscription> createSubscription(SubscriptionDtoRequest subscriptionDtoRequest) {
        Subscription subscription = new Subscription();
        subscription.setName(subscriptionDtoRequest.getName());
        subscription.setSaleLimit(subscriptionDtoRequest.getSaleLimit());
        subscription.setPrice(subscriptionDtoRequest.getPrice());
        subscription.setPointRequired(subscriptionDtoRequest.getPointRequired());
        subscription.setDescription(subscriptionDtoRequest.getDescription());
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return apiResponseBuilder.buildResponse(savedSubscription, HttpStatus.CREATED, "Subscription created successfully");
    }

    @Override
    public Optional<ApiItemResponse<Subscription>> getSubscriptionById(UUID uuid) {
        Subscription pkg = subscriptionRepository.findById(uuid)
                .orElseThrow(() -> new AppException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
        return Optional.ofNullable(apiResponseBuilder.buildResponse(pkg, HttpStatus.OK, "Subscription found"));
    }

    @Override
    public ApiListResponse<Subscription> getAllSubscriptions(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Subscription> subscriptionPage = subscriptionRepository.findAll(pageable);
        List<Subscription> subscriptions = subscriptionPage.getContent();
        return ApiListResponse.<Subscription>builder()
                .data(subscriptions)
                .size(subscriptionPage.getSize())
                .page(subscriptionPage.getNumber())
                .totalSize((int) subscriptionPage.getTotalElements())
                .totalPage(subscriptionPage.getTotalPages())
                .message("All subscriptions retrieved")
                .status(HttpStatus.OK)
                .build();
    }




    @Override
    public ApiItemResponse<Subscription> updateSubscription(UUID uuid, SubscriptionDtoRequest pkgDto) {
        Subscription existingSubscription = subscriptionRepository.findById(uuid)
                .orElseThrow(() -> new AppException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
        existingSubscription.setName(pkgDto.getName());
        existingSubscription.setSaleLimit(pkgDto.getSaleLimit());
        existingSubscription.setPrice(pkgDto.getPrice());
        Subscription updatedSubscription = subscriptionRepository.save(existingSubscription);
        return apiResponseBuilder.buildResponse(updatedSubscription, HttpStatus.OK, "Subscription updated successfully");
    }

    @Override
    public ApiItemResponse<Void> deleteSubscription(UUID uuid) {
        if (!subscriptionRepository.existsById(uuid)) {
            throw new AppException(ErrorCode.SUBSCRIPTION_NOT_FOUND);
        }
        subscriptionRepository.deleteById(uuid);
        return apiResponseBuilder.buildResponse(null, HttpStatus.OK, "Subscription deleted successfully");
    }

}
