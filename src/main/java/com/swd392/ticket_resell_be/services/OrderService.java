package com.swd392.ticket_resell_be.services;

import com.swd392.ticket_resell_be.dtos.requests.OrderDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.OrderDtoResponse;
import com.swd392.ticket_resell_be.entities.ChatBox;
import com.swd392.ticket_resell_be.entities.Order;
import org.springframework.data.domain.Sort;

import java.util.UUID;

public interface OrderService {
    ApiItemResponse<OrderDtoResponse> createOrder(OrderDtoRequest orderDtoRequest);

    ApiItemResponse<OrderDtoResponse> updateOrder(UUID id, OrderDtoRequest orderDtoRequest);

    ApiItemResponse<OrderDtoResponse> removeOrder(UUID id);

    ApiListResponse<OrderDtoResponse> getAllOrdersForAdmin(int page, int size, Sort.Direction direction, String[] properties);

    ApiItemResponse<OrderDtoResponse> getOrderById(UUID id);

    Order findById(UUID id);

    Order findByChatBox(ChatBox chatBox);
}
