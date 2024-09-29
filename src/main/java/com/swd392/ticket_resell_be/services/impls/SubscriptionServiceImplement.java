package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.requests.PageDtoRequest;
import com.swd392.ticket_resell_be.dtos.requests.SubscriptionDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.entities.Subscription;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.SubscriptionRepository;
import com.swd392.ticket_resell_be.services.SubscriptionService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import com.swd392.ticket_resell_be.utils.PagingUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SubscriptionServiceImplement implements SubscriptionService {

    SubscriptionRepository subscriptionRepository;
    ApiResponseBuilder apiResponseBuilder;
    PagingUtil pagingUtil;

    @Override
    public ApiItemResponse<Subscription> createSubscription(SubscriptionDtoRequest subscriptionDtoRequest) {
        Subscription subscription = new Subscription();
        subscription.setName(subscriptionDtoRequest.name());
        subscription.setSaleLimit(subscriptionDtoRequest.saleLimit());
        subscription.setPrice(subscriptionDtoRequest.price());
        subscription.setPointRequired(subscriptionDtoRequest.pointRequired());
        subscription.setDescription(subscriptionDtoRequest.description());
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
        PageDtoRequest pageDtoRequest = new PageDtoRequest(size, page);
        Page<Subscription> subscriptionPage = subscriptionRepository.findAll(pagingUtil.getPageable(pageDtoRequest));
        List<Subscription> subscriptions = subscriptionPage.getContent();

        return apiResponseBuilder.buildResponse(subscriptions, subscriptionPage.getSize(),
                subscriptionPage.getNumber(), subscriptionPage.getTotalElements(),
                subscriptionPage.getTotalPages(), HttpStatus.OK, "All subscriptions retrieved");
    }

    @Override
    public ApiItemResponse<Subscription> updateSubscription(UUID uuid, SubscriptionDtoRequest pkgDto) {
        Subscription existingSubscription = subscriptionRepository.findById(uuid)
                .orElseThrow(() -> new AppException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
        existingSubscription.setName(pkgDto.name());
        existingSubscription.setSaleLimit(pkgDto.saleLimit());
        existingSubscription.setPrice(pkgDto.price());
        Subscription updatedSubscription = subscriptionRepository.save(existingSubscription);
        return apiResponseBuilder.buildResponse(updatedSubscription, HttpStatus.OK, "Subscription updated successfully");
    }

}
