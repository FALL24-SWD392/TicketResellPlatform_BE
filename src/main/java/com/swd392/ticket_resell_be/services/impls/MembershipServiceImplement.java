package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.entities.Membership;
import com.swd392.ticket_resell_be.entities.Subscription; // Import the Subscription entity
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.MembershipRepository;
import com.swd392.ticket_resell_be.services.MembershipService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MembershipServiceImplement implements MembershipService {

    MembershipRepository membershipRepository;
    ApiResponseBuilder apiResponseBuilder;

    @Override
    public ApiItemResponse<Membership> createMembership(User user, Subscription subscription) {
        if (user == null || subscription == null) {
            throw new AppException(ErrorCode.USER_SUBSCRIPTION_NOT_FOUND);
        }
        membershipRepository.deactivateActiveMemberships(user);
        Membership membership = new Membership();
        membership.setId(UUID.randomUUID());
        membership.setUser(user);
        membership.setSubscriptionName(subscription.getName());
        Date currentDate = new Date();
        membership.setStartDate(currentDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date endDate = calendar.getTime();
        membership.setEndDate(endDate);
        membership.setActive(true);
        Membership savedMembership = membershipRepository.save(membership);
        return apiResponseBuilder.buildResponse(savedMembership, HttpStatus.CREATED, "Membership created successfully");
    }


    @Override
    public ApiItemResponse<Membership> updateMembership(User user, Subscription subscription) {
        // Kiểm tra đầu vào
        if (user == null || subscription == null) {
            throw new AppException(ErrorCode.USER_SUBSCRIPTION_NOT_FOUND);
        }

        // Tìm kiếm membership hiện tại của người dùng
        Optional<Membership> existingMembership = membershipRepository.findMembershipByUser(user);
        Membership membership;
        if (existingMembership.isPresent()) {
            membership = existingMembership.get();
            membership.setSubscriptionName(subscription.getName());
            membership.setSaleRemain(subscription.getSaleLimit());
            Date currentDate = new Date();
            membership.setStartDate(currentDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_MONTH, 30);
            Date endDate = calendar.getTime();
            membership.setEndDate(endDate);
        } else {
            membership = new Membership();
            membership.setId(UUID.randomUUID());
            membership.setUser(user);
            membership.setSubscriptionName(subscription.getName());
            membership.setSaleRemain(subscription.getSaleLimit());
            Date currentDate = new Date();
            membership.setStartDate(currentDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_MONTH, 30);
            Date endDate = calendar.getTime();
            membership.setEndDate(endDate);
            membership.setActive(true);
        }
        Membership savedMembership = membershipRepository.save(membership);
        return apiResponseBuilder.buildResponse(savedMembership, HttpStatus.OK, "Membership updated successfully");
    }

}
