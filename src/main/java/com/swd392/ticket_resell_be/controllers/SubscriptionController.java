package com.swd392.ticket_resell_be.controllers;

import com.swd392.ticket_resell_be.dtos.requests.SubscriptionDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.entities.Subscription;
import com.swd392.ticket_resell_be.services.SubscriptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/subscriptions")
@Tag(name = "Subscription APIs")
public class SubscriptionController {
    SubscriptionService subscriptionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiItemResponse<Subscription>> createSubscription(@RequestBody @Valid SubscriptionDtoRequest subscriptionDtoRequest) {
        return ResponseEntity.ok(subscriptionService.createSubscription(subscriptionDtoRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ApiItemResponse<Subscription>>> getSubscriptionById(@PathVariable("id") UUID packageId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionById(packageId));
    }

    @GetMapping
    public ResponseEntity<ApiListResponse<Subscription>> getAllSubscriptions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions(page, size));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    public ResponseEntity<ApiItemResponse<Subscription>> updateSubscription(
            @PathVariable("id") UUID subscriptionId,
            @RequestBody @Valid SubscriptionDtoRequest subscriptionDtoRequest) {
        return ResponseEntity.ok(subscriptionService.handleUpdateSubscription(subscriptionId, subscriptionDtoRequest));
    }

    @PostMapping("/purchase-subscription")
    public ResponseEntity<ApiItemResponse<String>> submitOrder(
            @RequestParam("subscriptionId") UUID subscriptionId,
            HttpServletRequest request) {
        return ResponseEntity.ok(subscriptionService.purchaseSubscription(subscriptionId, request));
    }
}





