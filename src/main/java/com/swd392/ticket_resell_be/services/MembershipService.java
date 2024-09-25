package com.swd392.ticket_resell_be.services;

import com.swd392.ticket_resell_be.entities.Membership;
import com.swd392.ticket_resell_be.entities.Subscription;
import com.swd392.ticket_resell_be.entities.User;

public interface MembershipService {

    Membership create(User user, Subscription subscriptionField);
}
