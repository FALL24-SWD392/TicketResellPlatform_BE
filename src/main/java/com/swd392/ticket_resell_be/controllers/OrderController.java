package com.swd392.ticket_resell_be.controllers;

import com.swd392.ticket_resell_be.dtos.requests.OrderDetailDtoRequest;
import com.swd392.ticket_resell_be.dtos.requests.OrderDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.OrderDetailDtoResponse;
import com.swd392.ticket_resell_be.dtos.responses.OrderDtoResponse;
import com.swd392.ticket_resell_be.services.OrderDetailService;
import com.swd392.ticket_resell_be.services.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/orders")
@Tag(name = "Order APIs")
public class OrderController {
    OrderService orderService;
    OrderDetailService orderDetailService;

    @PostMapping
    public ResponseEntity<ApiItemResponse<OrderDtoResponse>> createOrder(
            @RequestBody OrderDtoRequest orderDtoRequest) {
        return ResponseEntity.ok(orderService.createOrder(orderDtoRequest));
    }

    @PutMapping
    public ResponseEntity<ApiItemResponse<OrderDtoResponse>> updateOrder(
            @RequestParam UUID id,
            @RequestBody OrderDtoRequest orderDtoRequest) {
        return ResponseEntity.ok(orderService.updateOrder(id, orderDtoRequest));
    }

    @DeleteMapping
    public ResponseEntity<ApiItemResponse<OrderDtoResponse>> deleteOrder(
            @RequestParam UUID id) {
        return ResponseEntity.ok(orderService.removeOrder(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/admin")
    public ResponseEntity<ApiListResponse<OrderDtoResponse>> getAllOrdersForAdmin(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(defaultValue = "id") String... properties) {
        return ResponseEntity.ok(orderService.getAllOrdersForAdmin(page - 1, size, direction, properties));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiItemResponse<OrderDtoResponse>> getOrderById(
            @PathVariable("id") UUID id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping("/details")
    public ResponseEntity<ApiItemResponse<OrderDetailDtoResponse>> createOrderDetail(
            @RequestBody OrderDetailDtoRequest orderDetailDtoRequest) {
        return ResponseEntity.ok(orderDetailService.createOrderDetail(orderDetailDtoRequest));
    }

    @PutMapping("/details")
    public ResponseEntity<ApiItemResponse<OrderDetailDtoResponse>> updateOrderDetail(
            @RequestParam UUID id,
            @RequestBody OrderDetailDtoRequest orderDetailDtoRequest) {
        return ResponseEntity.ok(orderDetailService.updateOrderDetail(id, orderDetailDtoRequest));
    }

    @DeleteMapping("/details")
    public ResponseEntity<ApiItemResponse<OrderDetailDtoResponse>> deleteOrderDetail(
            @RequestParam UUID id) {
        return ResponseEntity.ok(orderDetailService.removeOrderDetail(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/details/admin")
    public ResponseEntity<ApiListResponse<OrderDetailDtoResponse>> getAllOrderDetailsForAdmin(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(defaultValue = "id") String... properties) {
        return ResponseEntity.ok(orderDetailService.getAllOrderDetailsForAdmin(page - 1, size, direction, properties));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<ApiItemResponse<OrderDetailDtoResponse>> getOrderDetailById(
            @PathVariable("id") UUID id) {
        return ResponseEntity.ok(orderDetailService.getOrderDetailById(id));
    }
}
