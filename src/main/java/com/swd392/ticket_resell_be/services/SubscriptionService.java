package com.swd392.ticket_resell_be.services;

import com.swd392.ticket_resell_be.entities.Package;
import com.swd392.ticket_resell_be.entities.Subscription;
import com.swd392.ticket_resell_be.entities.User;

public interface SubscriptionService {

    Subscription createSubscription(User user, Package packageField);
}
