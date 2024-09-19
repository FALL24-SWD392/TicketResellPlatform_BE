package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.requests.SubscriptionDtoRequest;
import com.swd392.ticket_resell_be.entities.Package;
import com.swd392.ticket_resell_be.entities.Subscription;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.PackageRepository;
import com.swd392.ticket_resell_be.repositories.SubscriptionRepository;
import com.swd392.ticket_resell_be.repositories.UserRepository;
import com.swd392.ticket_resell_be.services.SubscriptionService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SubscriptionServiceImplement implements SubscriptionService {

    SubscriptionRepository subscriptionRepository;
    UserRepository userRepository;
    PackageRepository packageRepository;
    ApiResponseBuilder apiResponseBuilder;

    @Override
    public void createSubscription(SubscriptionDtoRequest subscriptionDtoRequest) {
        // Create a new Subscription object
        Subscription subscription = new Subscription();

        // Fetch User and Package by their IDs
        User user = userRepository.findById(subscriptionDtoRequest.user().getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Package packageField = packageRepository.findById(subscriptionDtoRequest.aPackage().getId())
                .orElseThrow(() -> new AppException(ErrorCode.PACKAGE_NOT_FOUND));

        // Set fields on the Subscription object
        subscription.setUser(user);
        subscription.setPackageField(packageField);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusDays(packageField.getDuration()));
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        apiResponseBuilder.buildResponse(savedSubscription, HttpStatus.CREATED, "Subscription created successfully");
    }

}
