package com.swd392.ticket_resell_be.services;

import com.swd392.ticket_resell_be.dtos.requests.OrderDetailDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.OrderDetailDtoResponse;
import com.swd392.ticket_resell_be.entities.OrderDetail;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface OrderDetailService {
    ApiListResponse<OrderDetailDtoResponse> getAllOrderDetailsForAdmin(int page, int size, Sort.Direction direction, String[] properties);

    ApiItemResponse<OrderDetailDtoResponse> getOrderDetailById(UUID id);

    ApiListResponse<OrderDetailDtoResponse> getAllOrderDetailsForOrder(UUID orderId, int page, int size, Sort.Direction direction, String... properties);

    boolean createOrderDetail(OrderDetailDtoRequest orderDetailDtoRequest);

    OrderDetail getAllOrderDetailsForOrderToDto(UUID id);
}
