package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.entities.Package; // Import the Package entity
import com.swd392.ticket_resell_be.entities.Subscription;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.repositories.SubscriptionRepository;
import com.swd392.ticket_resell_be.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImplement implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription createSubscription(User user, Package packageField) {
        Subscription subscription = new Subscription();
        subscription.setId(UUID.randomUUID());
        subscription.setUser(user);
        subscription.setPackageField(packageField);
        subscription.setStartDate(new Date());
        if (packageField != null && packageField.getDuration() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, packageField.getDuration());
            subscription.setEndDate(calendar.getTime());
        }        subscription.setActive(true);
        return subscriptionRepository.save(subscription);
    }
}
