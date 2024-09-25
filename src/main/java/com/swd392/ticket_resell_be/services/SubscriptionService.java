package com.swd392.ticket_resell_be.services;

import com.swd392.ticket_resell_be.dtos.requests.SubscriptionDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.entities.Subscription;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionService {
    ApiItemResponse<Subscription> createSubscription(SubscriptionDtoRequest pkgDto);
    Optional<ApiItemResponse<Subscription>> getSubscriptionById(UUID uuid);
    ApiItemResponse<List<Subscription>> getAllSubscriptions();
    ApiItemResponse<Subscription> updateSubscription(UUID uuid, SubscriptionDtoRequest pkgDto);
    ApiItemResponse<Void> deleteSubscription(UUID uuid);

}
