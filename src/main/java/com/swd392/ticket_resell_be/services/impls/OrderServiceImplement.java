package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.requests.OrderDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.OrderDtoResponse;
import com.swd392.ticket_resell_be.entities.Order;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.OrderRepository;
import com.swd392.ticket_resell_be.services.ChatBoxService;
import com.swd392.ticket_resell_be.services.OrderService;
import com.swd392.ticket_resell_be.utils.ApiResponseBuilder;
import com.swd392.ticket_resell_be.utils.PagingUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderServiceImplement implements OrderService {
    OrderRepository orderRepository;
    ApiResponseBuilder apiResponseBuilder;
    PagingUtil pagingUtil;
    ChatBoxService chatBoxService;


    @Override
    public ApiItemResponse<OrderDtoResponse> createOrder(OrderDtoRequest orderDtoRequest) {
        Order order = new Order();
        mapperHandmade(order, orderDtoRequest);
        order.setStatus(Categorize.COMPLETED);

        orderRepository.save(order);
        return apiResponseBuilder.buildResponse(
                parseToOrderDtoResponse(order),
                HttpStatus.CREATED
        );
    }

    private void mapperHandmade(Order order, OrderDtoRequest orderDtoRequest) {
        if (chatBoxService.findById(orderDtoRequest.chatBoxId()) != null) {
            order.setChatBox(chatBoxService.findById(orderDtoRequest.chatBoxId()));
            order.setStatus(orderDtoRequest.status());
        }
    }

    private OrderDtoResponse parseToOrderDtoResponse(Order order) {
        OrderDtoResponse orderDtoResponse = new OrderDtoResponse();
        orderDtoResponse.setId(order.getId());
        orderDtoResponse.setChatBoxId(order.getChatBox().getId());
        orderDtoResponse.setStatus(order.getStatus());

        return orderDtoResponse;
    }

    @Override
    public ApiItemResponse<OrderDtoResponse> updateOrder(UUID id, OrderDtoRequest orderDtoRequest) {
        Order existingOrder = orderRepository.findById(id);
        if (existingOrder == null)
            throw new AppException(ErrorCode.ORDER_DOES_NOT_EXIST);
        mapperHandmade(existingOrder, orderDtoRequest);
        existingOrder.setId(id);
        orderRepository.save(existingOrder);

        return apiResponseBuilder.buildResponse(
                parseToOrderDtoResponse(existingOrder),
                HttpStatus.OK
        );
    }

    @Override
    public ApiItemResponse<OrderDtoResponse> removeOrder(UUID id) {
        Order order = orderRepository.findById(id);
        order.setStatus(Categorize.REMOVED);
        orderRepository.save(order);

        return apiResponseBuilder.buildResponse(
                parseToOrderDtoResponse(order),
                HttpStatus.OK
        );
    }

    @Override
    public ApiListResponse<OrderDtoResponse> getAllOrdersForAdmin(int page, int size, Sort.Direction direction, String[] properties) {
        Page<Order> orders = orderRepository.findAll(pagingUtil
                .getPageable(Order.class, page, size, direction, properties));
        if (orders.isEmpty())
            throw new AppException(ErrorCode.ORDER_DOES_NOT_EXIST);
        else
            return apiResponseBuilder.buildResponse(
                    parseToOrderDtoResponses(orders),
                    orders.getSize(),
                    orders.getNumber() + 1,
                    orders.getTotalElements(),
                    orders.getTotalPages(),
                    HttpStatus.OK,
                    null
            );
    }

    private List<OrderDtoResponse> parseToOrderDtoResponses(Page<Order> orders) {
        return orders.getContent().stream()
                .map(order -> new OrderDtoResponse(
                        order.getId(),
                        order.getChatBox().getId(),
                        order.getStatus()))
                .toList();
    }

    @Override
    public ApiItemResponse<OrderDtoResponse> getOrderById(UUID id) {
        Order order = orderRepository.findById(id);
        if (order == null)
            throw new AppException(ErrorCode.ORDER_DOES_NOT_EXIST);
        else
            return apiResponseBuilder.buildResponse(
                    parseToOrderDtoResponse(order),
                    HttpStatus.OK
            );
    }

    @Override
    public Order findById(UUID id) {
        return orderRepository.findById(id);
    }
}
