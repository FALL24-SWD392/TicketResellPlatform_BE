package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.entities.Membership;
import com.swd392.ticket_resell_be.entities.Subscription; // Import the Subscription entity
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.repositories.MembershipRepository;
import com.swd392.ticket_resell_be.services.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MembershipServiceImplement implements MembershipService {

    private final MembershipRepository membershipRepository;

    @Override
    public Membership create(User user, Subscription subscriptionField) {
        Membership membership = new Membership();
        membership.setId(UUID.randomUUID());
        membership.setUser(user);
        membership.setPackageField(subscriptionField);
        membership.setStartDate(new Date());
        if (subscriptionField != null && subscriptionField.getDuration() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, subscriptionField.getDuration());
            membership.setEndDate(calendar.getTime());
        }        membership.setActive(true);
        return membershipRepository.save(membership);
    }
}
