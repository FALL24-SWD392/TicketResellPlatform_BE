package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.MembershipDtoResponse;
import com.swd392.ticket_resell_be.entities.Membership;
import com.swd392.ticket_resell_be.entities.Subscription;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.MembershipRepository;
import com.swd392.ticket_resell_be.services.MembershipService;
import com.swd392.ticket_resell_be.services.TicketService;
import com.swd392.ticket_resell_be.services.UserService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    TicketService ticketService;
    UserService userService;
    @Override
    public ApiItemResponse<Membership> createMembership(User user, Subscription subscription) {
        if (user == null || subscription == null) {
            throw new AppException(ErrorCode.USER_SUBSCRIPTION_NOT_FOUND);
        }
        Membership membership = new Membership();
        membership.setId(UUID.randomUUID());
        membership.setSeller(user);
        membership.setSubscriptionName(subscription.getName());
        Date currentDate = new Date();
        membership.setStartDate(currentDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        Date endDate = calendar.getTime();
        membership.setEndDate(endDate);
        Membership savedMembership = membershipRepository.save(membership);
        return apiResponseBuilder.buildResponse(savedMembership, HttpStatus.CREATED, "Membership created successfully");
    }

    @Override
    public ApiItemResponse<Membership> updateMembership(User user, Subscription subscription) {
        if (user == null || subscription == null) {
            throw new AppException(ErrorCode.USER_SUBSCRIPTION_NOT_FOUND);
        }
        Optional<Membership> existingMembership = membershipRepository.findMembershipBySeller(user);
        Membership membership;
        int ticketCount = ticketService.getCountBySellerAndStatus(user, Categorize.APPROVED);
        int saleRemain = subscription.getSaleLimit() - ticketCount;

        if (existingMembership.isPresent()) {
            membership = existingMembership.get();
            membership.setSubscriptionName(subscription.getName());
            membership.setSaleRemain(saleRemain);
            Date currentDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_MONTH, 30);
            Date endDate = calendar.getTime();
            membership.setEndDate(endDate);
        } else {
            membership = new Membership();
            membership.setId(UUID.randomUUID());
            membership.setSeller(user);
            membership.setSubscriptionName(subscription.getName());
            membership.setSaleRemain(subscription.getSaleLimit());
            Date currentDate = new Date();
            membership.setStartDate(currentDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_MONTH, 30);
            Date endDate = calendar.getTime();
            membership.setEndDate(endDate);
        }
        Membership savedMembership = membershipRepository.save(membership);
        return apiResponseBuilder.buildResponse(savedMembership, HttpStatus.OK, "Membership updated successfully");
    }

    @Override
    public ApiItemResponse<MembershipDtoResponse> getMembershipForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByName(username)
                .orElseThrow(()-> new AppException(ErrorCode.USER_NOT_FOUND));
        Membership membership = membershipRepository.findMembershipBySeller(user)
                .orElseThrow(() -> new AppException(ErrorCode.MEMBERSHIP_NOT_FOUND));
        MembershipDtoResponse membershipDtoResponse = mapToDto(membership);

        return apiResponseBuilder.buildResponse(membershipDtoResponse, HttpStatus.OK, "Membership retrieved successfully");
    }

    private MembershipDtoResponse mapToDto(Membership membership) {
        return MembershipDtoResponse.builder()
                .id(membership.getId())
                .subscriptionName(membership.getSubscriptionName())
                .saleRemain(membership.getSaleRemain())
                .startDate(membership.getStartDate())
                .endDate(membership.getEndDate())
                .build();
    }


}
