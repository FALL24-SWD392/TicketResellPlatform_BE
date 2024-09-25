package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.requests.SubscriptionDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.entities.Subscription;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.SubscriptionRepository;
import com.swd392.ticket_resell_be.repositories.UserRepository;
import com.swd392.ticket_resell_be.services.SubscriptionService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SubscriptionServiceImplement implements SubscriptionService {

    SubscriptionRepository subscriptionRepository;
    ApiResponseBuilder apiResponseBuilder;
    UserRepository userRepository;


    @Override
    public ApiItemResponse<Subscription> createSubscription(SubscriptionDtoRequest pkgDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User createdByUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Subscription pkg = new Subscription();
        pkg.setPackageName(pkgDto.getSubscriptionName());
        pkg.setSaleLimit(pkgDto.getSaleLimit());
        pkg.setPrice(pkgDto.getPrice());
        pkg.setImageUrls(pkgDto.getImageUrls());
        pkg.setCreatedBy(createdByUser);
        pkg.setDuration(pkgDto.getDuration());
        pkg.setStatus(pkgDto.isActive());
        Subscription savedSubscription = subscriptionRepository.save(pkg);
        return apiResponseBuilder.buildResponse(savedSubscription, HttpStatus.CREATED, "Subscription created successfully");
    }

    @Override
    public Optional<ApiItemResponse<Subscription>> getSubscriptionById(UUID uuid) {
        Subscription pkg = subscriptionRepository.findById(uuid)
                .orElseThrow(() -> new AppException(ErrorCode.PACKAGE_NOT_FOUND));
        return Optional.ofNullable(apiResponseBuilder.buildResponse(pkg, HttpStatus.OK, "Subscription found"));
    }

    @Override
    public ApiItemResponse<List<Subscription>> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        return apiResponseBuilder.buildResponse(subscriptions, HttpStatus.OK, "All subscriptions retrieved");
    }

    @Override
    public ApiItemResponse<Subscription> updateSubscription(UUID uuid, SubscriptionDtoRequest pkgDto) {
        Subscription existingSubscription = subscriptionRepository.findById(uuid)
                .orElseThrow(() -> new AppException(ErrorCode.PACKAGE_NOT_FOUND));
        existingSubscription.setPackageName(pkgDto.getSubscriptionName());
        existingSubscription.setSaleLimit(pkgDto.getSaleLimit());
        existingSubscription.setPrice(pkgDto.getPrice());
        existingSubscription.setImageUrls(pkgDto.getImageUrls());
        existingSubscription.setDuration(pkgDto.getDuration());
        existingSubscription.setStatus(pkgDto.isActive());
        Subscription updatedSubscription = subscriptionRepository.save(existingSubscription);
        return apiResponseBuilder.buildResponse(updatedSubscription, HttpStatus.OK, "Subscription updated successfully");
    }

    @Override
    public ApiItemResponse<Void> deleteSubscription(UUID uuid) {
        if (!subscriptionRepository.existsById(uuid)) {
            throw new AppException(ErrorCode.PACKAGE_NOT_FOUND);
        }
        subscriptionRepository.deleteById(uuid);
        return apiResponseBuilder.buildResponse(null, HttpStatus.OK, "Subscription deleted successfully");
    }






}
