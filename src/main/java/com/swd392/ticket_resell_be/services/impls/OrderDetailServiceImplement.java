package com.swd392.ticket_resell_be.services.impls;

import com.swd392.ticket_resell_be.dtos.requests.OrderDetailDtoRequest;
import com.swd392.ticket_resell_be.dtos.responses.ApiItemResponse;
import com.swd392.ticket_resell_be.dtos.responses.ApiListResponse;
import com.swd392.ticket_resell_be.dtos.responses.OrderDetailDtoResponse;
import com.swd392.ticket_resell_be.entities.Order;
import com.swd392.ticket_resell_be.entities.OrderDetail;
import com.swd392.ticket_resell_be.entities.Ticket;
import com.swd392.ticket_resell_be.enums.Categorize;
import com.swd392.ticket_resell_be.enums.ErrorCode;
import com.swd392.ticket_resell_be.exceptions.AppException;
import com.swd392.ticket_resell_be.repositories.OrderDetailRepository;
import com.swd392.ticket_resell_be.repositories.OrderRepository;
import com.swd392.ticket_resell_be.services.OrderDetailService;
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
public class OrderDetailServiceImplement implements OrderDetailService {
    OrderDetailRepository orderDetailRepository;
    OrderRepository orderRepository;
    TicketService ticketService;
    ApiResponseBuilder apiResponseBuilder;
    PagingUtil pagingUtil;

    public boolean createOrderDetail(OrderDetailDtoRequest orderDetailDtoRequest) {
        OrderDetail orderDetail = new OrderDetail();
        mapperHandmade(orderDetail, orderDetailDtoRequest);
        Ticket ticket = ticketService.getTicketById(orderDetail.getOrder().getChatBox().getTicket().getId());
        if(ticket.getQuantity() < orderDetail.getQuantity())
            return false;
        else{
            int newQuantity = ticket.getQuantity() - orderDetail.getQuantity();
            minusTicketQuantity(newQuantity, ticket);
            orderDetailRepository.save(orderDetail);
            return true;
        }
    }

    private void minusTicketQuantity(int newQuantity, Ticket ticket) {
        if (newQuantity == 0) {
            ticketService.updateTicketQuantity(ticket.getId(), 0);
            ticketService.updateTicketStatus(ticket.getId(), Categorize.SOLD);
        } else {
            ticket.setQuantity(newQuantity);
            ticketService.updateTicketQuantity(ticket.getId(), newQuantity);
        }
    }

    private void mapperHandmade(OrderDetail orderDetail, OrderDetailDtoRequest orderDetailDtoRequest) {
        orderDetail.setOrder(orderRepository.findById(orderDetailDtoRequest.orderId()));
        orderDetail.setTicketId(orderDetailDtoRequest.ticketId());
        orderDetail.setQuantity(orderDetailDtoRequest.quantity());
    }

    private OrderDetailDtoResponse parseToOrderDetailDtoResponse(OrderDetail orderDetail) {
        OrderDetailDtoResponse orderDetailDtoResponse = new OrderDetailDtoResponse();
        orderDetailDtoResponse.setId(orderDetail.getId());
        orderDetailDtoResponse.setOrderId(orderDetail.getOrder().getId());
        orderDetailDtoResponse.setTicketId(orderDetail.getTicketId());
        orderDetailDtoResponse.setQuantity(orderDetail.getQuantity());

        return orderDetailDtoResponse;
    }

    private List<OrderDetailDtoResponse> parseToOrderDtoResponses(Page<OrderDetail> orderDetails) {
        return orderDetails.getContent().stream()
                .map(orderDetail -> new OrderDetailDtoResponse(
                        orderDetail.getId(),
                        orderDetail.getOrder().getId(),
                        orderDetail.getTicketId(),
                        orderDetail.getQuantity()))
                .toList();
    }

    @Override
    public ApiListResponse<OrderDetailDtoResponse> getAllOrderDetailsForAdmin(int page, int size, Sort.Direction direction, String[] properties) {
        Page<OrderDetail> orderDetails = orderDetailRepository.findAll(pagingUtil
                .getPageable(Order.class, page, size, direction, properties));
        if (orderDetails.isEmpty())
            throw new AppException(ErrorCode.ORDER_DETAIL_DOES_NOT_EXIST);
        else
            return apiResponseBuilder.buildResponse(
                    parseToOrderDtoResponses(orderDetails),
                    orderDetails.getSize(),
                    orderDetails.getNumber() + 1,
                    orderDetails.getTotalElements(),
                    orderDetails.getTotalPages(),
                    HttpStatus.OK,
                    null
            );
    }

    @Override
    public ApiItemResponse<OrderDetailDtoResponse> getOrderDetailById(UUID id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id);
        if (orderDetail == null)
            throw new AppException(ErrorCode.ORDER_DETAIL_DOES_NOT_EXIST);
        else
            return apiResponseBuilder.buildResponse(
                    parseToOrderDetailDtoResponse(orderDetail),
                    HttpStatus.OK
            );
    }

    @Override
    public ApiListResponse<OrderDetailDtoResponse> getAllOrderDetailsForOrder(UUID orderId, int page, int size, Sort.Direction direction, String... properties) {
        Page<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId, pagingUtil
                .getPageable(OrderDetail.class, page, size, direction, properties));
        if (orderDetails.isEmpty())
            throw new AppException(ErrorCode.ORDER_DOES_NOT_EXIST);
        else
            return apiResponseBuilder.buildResponse(
                    parseToOrderDtoResponses(orderDetails),
                    orderDetails.getSize(),
                    orderDetails.getNumber() + 1,
                    orderDetails.getTotalElements(),
                    orderDetails.getTotalPages(),
                    HttpStatus.OK,
                    null
            );
    }


}
