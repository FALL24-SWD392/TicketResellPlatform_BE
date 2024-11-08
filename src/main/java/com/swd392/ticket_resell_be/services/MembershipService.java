package com.swd392.ticket_resell_be.services;

import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.MembershipDtoResponse;
import com.swd392.ticket_resell_be.entities.Membership;
import com.swd392.ticket_resell_be.entities.Subscription;
import com.swd392.ticket_resell_be.entities.User;

import java.util.Optional;

public interface MembershipService {

    ApiItemResponse<Membership> updateMembership(User user, Subscription subscriptionField);


    ApiItemResponse<MembershipDtoResponse> createFreeMembershipForLoggedInUser();

    Membership getMembershipForLoggedInUser(User user);

    void createInitialMembership();

    void updateExpiredMembership();

    Optional<Membership> findMembershipBySeller(User user);
}
