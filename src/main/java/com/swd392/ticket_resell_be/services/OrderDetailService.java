package com.swd392.ticket_resell_be.services;

import com.swd392.ticket_resell_be.dtos.requests.OrderDetailDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.OrderDetailDtoResponse;
import org.springframework.data.domain.Sort;

import java.util.UUID;

public interface OrderDetailService {
    ApiItemResponse<OrderDetailDtoResponse> createOrderDetail(OrderDetailDtoRequest orderDetailDtoRequest);

    ApiItemResponse<OrderDetailDtoResponse> updateOrderDetail(UUID id, OrderDetailDtoRequest orderDetailDtoRequest);

    ApiItemResponse<OrderDetailDtoResponse> removeOrderDetail(UUID id);

    ApiListResponse<OrderDetailDtoResponse> getAllOrderDetailsForAdmin(int page, int size, Sort.Direction direction, String[] properties);

    ApiItemResponse<OrderDetailDtoResponse> getOrderDetailById(UUID id);
}
