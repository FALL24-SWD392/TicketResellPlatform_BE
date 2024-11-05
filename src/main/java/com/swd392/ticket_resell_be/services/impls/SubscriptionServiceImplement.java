package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.requests.SubscriptionDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.VNPayOrderResponse;
import com.swd392.ticket_resell_be.entities.Subscription;
import com.swd392.ticket_resell_be.entities.User;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.SubscriptionRepository;
import com.swd392.ticket_resell_be.services.SubscriptionService;
import com.swd392.ticket_resell_be.services.TransactionService;
import com.swd392.ticket_resell_be.services.UserService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SubscriptionServiceImplement implements SubscriptionService {

    SubscriptionRepository subscriptionRepository;
    ApiResponseBuilder apiResponseBuilder;
    VNPayServiceImplement vnPayService;
    UserService userService;
    TransactionService transactionService;

    @Override
    public ApiItemResponse<Subscription> createSubscription(SubscriptionDtoRequest subscriptionDtoRequest) {
        Subscription subscription = new Subscription();
        subscription.setName(subscriptionDtoRequest.name());
        subscription.setSaleLimit(subscriptionDtoRequest.saleLimit());
        subscription.setPrice(subscriptionDtoRequest.price());
        subscription.setPointRequired(subscriptionDtoRequest.pointRequired());
        subscription.setDescription(subscriptionDtoRequest.description());
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return apiResponseBuilder.buildResponse(savedSubscription, HttpStatus.CREATED, "Subscription created successfully");
    }

    @Override
    public Optional<ApiItemResponse<Subscription>> getSubscriptionById(UUID uuid) {
        Subscription pkg = subscriptionRepository.findById(uuid)
                .orElseThrow(() -> new AppException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
        return Optional.ofNullable(apiResponseBuilder.buildResponse(pkg, HttpStatus.OK, "Subscription found"));
    }

    @Override
    public ApiListResponse<Subscription> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findAll();

        return apiResponseBuilder.buildResponse(subscriptions, 0, 0, 0, 0,
                HttpStatus.OK, "All subscriptions retrieved");
    }

    @Override
    public ApiItemResponse<Subscription> handleUpdateSubscription(UUID packageId, SubscriptionDtoRequest subscriptionDtoRequest) {
        try {
            Subscription existingSubscription = subscriptionRepository.findById(packageId)
                    .orElseThrow(() -> new AppException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
            Optional.ofNullable(subscriptionDtoRequest.name())
                    .filter(name -> !name.isEmpty())
                    .ifPresent(existingSubscription::setName);
            Optional.of(subscriptionDtoRequest.saleLimit())
                    .filter(saleLimit -> saleLimit != 0)
                    .ifPresent(existingSubscription::setSaleLimit);
            Optional.of(subscriptionDtoRequest.price())
                    .filter(price -> price != 0)
                    .ifPresent(existingSubscription::setPrice);
            Subscription updatedSubscription = subscriptionRepository.save(existingSubscription);
            return apiResponseBuilder.buildResponse(updatedSubscription, HttpStatus.OK, "Subscription updated successfully");
        } catch (AppException e) {
            return apiResponseBuilder.buildResponse(null, HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ConstraintViolationException e) {
            return apiResponseBuilder.buildResponse(null, HttpStatus.BAD_REQUEST, "Validation failed: " + e.getMessage());
        } catch (Exception e) {
            return apiResponseBuilder.buildResponse(null, HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage());
        }
    }

    @Override
    public ApiItemResponse<String> purchaseSubscription(UUID subscriptionId, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserByName(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Subscription subscription = getSubscriptionById(subscriptionId)
                .orElseThrow(() -> new AppException(ErrorCode.SUBSCRIPTION_NOT_FOUND)).data();
        if (user.getReputation() < subscription.getPointRequired()) {
            throw new AppException(ErrorCode.INSUFFICIENT_REPUTATION);
        }
        long orderTotal = (long) subscription.getPrice();
        String orderInfo = "Thanh toán cho gói " + subscription.getName();
        VNPayOrderResponse orderResponse = vnPayService.createOrder(orderTotal, orderInfo, request);
        transactionService.savePendingTransaction(subscription, user, orderResponse.orderCode());
        return apiResponseBuilder.buildResponse(orderResponse.vnPayUrl(), HttpStatus.OK, "Redirect to Purchase");
    }

    @Override
    public Subscription getSubscriptionByName(String name) {
        return subscriptionRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
    }

}
