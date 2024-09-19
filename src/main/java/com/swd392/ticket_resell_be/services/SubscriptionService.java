package com.swd392.ticket_resell_be.services;

import com.swd392.ticket_resell_be.dtos.requests.SubscriptionDtoRequest;

public interface SubscriptionService {
    void createSubscription(SubscriptionDtoRequest subscriptionDtoRequest);
}
