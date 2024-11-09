package com.swd392.ticket_resell_be.repositories;

import com.swd392.ticket_resell_be.entities.Order;
import com.swd392.ticket_resell_be.entities.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;
import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    OrderDetail findById(UUID id);

    Page<OrderDetail> findByOrderId(UUID orderId, Pageable pageable);

    OrderDetail findByOrderId(UUID orderId);
}
