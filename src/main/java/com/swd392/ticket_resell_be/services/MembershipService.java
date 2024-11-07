package com.swd392.ticket_resell_be.services;

import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.MembershipDtoResponse;
import com.swd392.ticket_resell_be.entities.Membership;
import com.swd392.ticket_resell_be.entities.Subscription;
import com.swd392.ticket_resell_be.entities.User;

public interface MembershipService {

    ApiItemResponse<Membership> updateMembership(User user, Subscription subscriptionField);

    ApiItemResponse<MembershipDtoResponse> getMembershipForUser();

    ApiItemResponse<MembershipDtoResponse> createFreeMembershipForLoggedInUser();
}
