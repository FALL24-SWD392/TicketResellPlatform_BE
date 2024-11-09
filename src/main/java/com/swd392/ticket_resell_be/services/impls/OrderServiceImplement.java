package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.requests.OrderDetailDtoRequest;
import com.swd392.ticket_resell_be.dtos.requests.OrderDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.OrderDtoResponse;
import com.swd392.ticket_resell_be.entities.ChatBox;
import com.swd392.ticket_resell_be.entities.Order;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.OrderRepository;
import com.swd392.ticket_resell_be.services.ChatBoxService;
import com.swd392.ticket_resell_be.services.OrderDetailService;
import com.swd392.ticket_resell_be.services.OrderService;
import com.swd392.ticket_resell_be.services.TicketService;
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
    OrderDetailService orderDetailService;
    TicketService ticketService;
    ApiResponseBuilder apiResponseBuilder;
    PagingUtil pagingUtil;
    ChatBoxService chatBoxService;


    @Override
    public ApiItemResponse<OrderDtoResponse> createOrder(OrderDtoRequest orderDtoRequest) {
        chatBoxService.createChatBox(orderDtoRequest.chatBoxId(), orderDtoRequest.senderId(), orderDtoRequest.recipientId(), orderDtoRequest.ticketId());

        Order order = new Order();
        mapperHandmade(order, orderDtoRequest);
        order.setStatus(Categorize.COMPLETED);
        orderRepository.save(order);

        OrderDetailDtoRequest orderDetailDtoRequest = new OrderDetailDtoRequest(order.getId(), orderDtoRequest.ticketId(), orderDtoRequest.quantity());

        if (orderDetailService.createOrderDetail(orderDetailDtoRequest)) {
            return apiResponseBuilder.buildResponse(
                    parseToOrderDtoResponse(order),
                    HttpStatus.CREATED,
                    "Create order successfully"
            );
        } else {
            return apiResponseBuilder.buildResponse(
                    null,
                    HttpStatus.CONFLICT,
                    "Create order failed"
            );
        }
    }

    private void mapperHandmade(Order order, OrderDtoRequest orderDtoRequest) {
        if (chatBoxService.findById(orderDtoRequest.chatBoxId()) != null)
            order.setChatBox(chatBoxService.findById(orderDtoRequest.chatBoxId()));
    }

    private OrderDtoResponse parseToOrderDtoResponse(Order order) {
        OrderDtoResponse orderDtoResponse = new OrderDtoResponse();
        orderDtoResponse.setId(order.getId());
        orderDtoResponse.setChatBoxId(order.getChatBox().getId());
        orderDtoResponse.setStatus(order.getStatus());
        orderDtoResponse.setTicket(ticketService.getTicketById(orderDetailService.getAllOrderDetailsForOrderToDto(order.getId()).getTicketId()));
        orderDtoResponse.setQuantity(orderDetailService.getAllOrderDetailsForOrderToDto(order.getId()).getQuantity());

        return orderDtoResponse;
    }

    @Override
    public ApiItemResponse<OrderDtoResponse> removeOrder(UUID id) {
        Order order = orderRepository.findById(id);
        order.setStatus(Categorize.REMOVED);
        orderRepository.save(order);

        return apiResponseBuilder.buildResponse(
                parseToOrderDtoResponse(order),
                HttpStatus.OK,
                "Remove order successfully"
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
                    "Get all orders successfully"
            );
    }

    private List<OrderDtoResponse> parseToOrderDtoResponses(Page<Order> orders) {
        return orders.getContent().stream()
                .map(order -> new OrderDtoResponse(
                        order.getId(),
                        order.getChatBox().getId(),
                        order.getStatus(),
                        ticketService.getTicketById(orderDetailService.getAllOrderDetailsForOrderToDto(order.getId()).getTicketId()),
                        orderDetailService.getAllOrderDetailsForOrderToDto(order.getId()).getQuantity()
                ))
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
                    HttpStatus.OK,
                    "Get order by id successfully"
            );
    }

    @Override
    public Order findById(UUID id) {
        return orderRepository.findById(id);
    }

    @Override
    public Order findByChatBox(ChatBox chatBox) {
        return orderRepository.findByChatBox(chatBox).orElse(null);
    }

    @Override
    public ApiListResponse<OrderDtoResponse> getAllOrdersForUser(UUID userId, int page, int size, Sort.Direction direction, String... properties) {

        Page<Order> orders = orderRepository.findByOrderChatBoxUserId(userId, pagingUtil
                .getPageable(Order.class, page, size, direction, properties));
        if (orders.isEmpty())
            throw new AppException(ErrorCode.ORDER_DOES_NOT_EXIST);
        else {
            List<OrderDtoResponse> orderDtoResponses = parseToOrderDtoResponses(orders);

            return apiResponseBuilder.buildResponse(
                    orderDtoResponses,
                    orders.getSize(),
                    orders.getNumber() + 1,
                    orders.getTotalElements(),
                    orders.getTotalPages(),
                    HttpStatus.OK,
                    "Get all orders for user successfully"
            );
        }

    }
}
