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
import com.swd392.ticket_resell_be.services.SubscriptionService;
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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MembershipServiceImplement implements MembershipService {

    MembershipRepository membershipRepository;
    ApiResponseBuilder apiResponseBuilder;
    UserService userService;
    SubscriptionService subscriptionService;
    @Override
    public ApiItemResponse<Membership> updateMembership(User user, Subscription subscription) {
        if (user == null || subscription == null) {
            throw new AppException(ErrorCode.USER_SUBSCRIPTION_NOT_FOUND);
        }
        Optional<Membership> existingMembership = membershipRepository.findMembershipBySeller(user);
        Membership membership;
        int saleRemain = subscription.getSaleLimit();
        if (existingMembership.isPresent()) {
            membership = existingMembership.get();
            membership.setSubscriptionName(subscription.getName());
            int ticketRemain = saleRemain + membership.getSaleRemain();
            membership.setSaleRemain(ticketRemain);
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
    public ApiItemResponse<MembershipDtoResponse> createFreeMembershipForLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Subscription subscription = subscriptionService.getSubscriptionByName("Free");
        Membership membership = membershipRepository.findMembershipBySeller(user)
                .orElse(null);
        if (membership == null) {
            membership = new Membership();
            membership.setSeller(user);
            membership.setSubscriptionName(subscription.getName());
            membership.setSaleRemain(subscription.getSaleLimit());
            membership.setStartDate(new Date());
            membership.setEndDate(Date.from(Instant.now().plus(30, ChronoUnit.DAYS)));
            membership = membershipRepository.save(membership);
        }
        MembershipDtoResponse membershipDtoResponse = mapToDto(membership);
        return apiResponseBuilder.buildResponse(membershipDtoResponse, HttpStatus.CREATED);
    }

    @Override
    public Membership getMembershipForLoggedInUser(User user) {
        return membershipRepository.findMembershipBySeller(user).get();
    }

    @Override
    public void createInitialMembership() {
        List<UUID> ids = new ArrayList<>();
        membershipRepository.findAll().forEach(membership -> ids.add(membership.getSeller().getId()));
        List<User> users = userService.findByIdNotIn(ids);
        Subscription subscription = subscriptionService.getSubscriptionByName("Free");
        users.forEach(user -> {
            Membership membership = new Membership();
            membership.setSubscriptionName(subscription.getName());
            membership.setSaleRemain(subscription.getSaleLimit());
            membership.setStartDate(new Date());
            membership.setEndDate(Date.from(Instant.now().plus(30, ChronoUnit.DAYS)));
            membership.setSeller(user);
            membershipRepository.save(membership);
        });
    }

    @Override
    public void updateExpiredMembership() {
        List<Membership> memberships = membershipRepository.findByEndDateBefore(new Date());
        Subscription subscription = subscriptionService.getSubscriptionByName("Free");
        memberships.forEach(membership -> {
            membership.setSubscriptionName(subscription.getName());
            membership.setSaleRemain(subscription.getSaleLimit());
            membership.setStartDate(new Date());
            membership.setEndDate(Date.from(Instant.now().plus(30, ChronoUnit.DAYS)));
            membershipRepository.save(membership);
        });
    }

    @Override
    public Optional<Membership> findMembershipBySeller(User user) {
        return membershipRepository.findMembershipBySeller(user);
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
