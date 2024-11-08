package com.swd392.ticket_resell_be.services;

import com.swd392.ticket_resell_be.dtos.requests.SubscriptionDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.SubscriptionDtoResponse;
import com.swd392.ticket_resell_be.entities.Subscription;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;
import java.util.UUID;

public interface SubscriptionService {
    ApiItemResponse<Subscription> createSubscription(SubscriptionDtoRequest pkgDto);

    Optional<ApiItemResponse<Subscription>> getSubscriptionById(UUID uuid);

    ApiListResponse<Subscription> getAllSubscriptions();

    ApiItemResponse<Subscription> handleUpdateSubscription(UUID packageId, SubscriptionDtoRequest subscriptionDtoRequest);

    ApiItemResponse<String> purchaseSubscription(UUID subscriptionId, HttpServletRequest request);

    Subscription getSubscriptionByName(String name);

    ApiListResponse<SubscriptionDtoResponse> getCurrentSubscriptionForLoggedInUser();

}
