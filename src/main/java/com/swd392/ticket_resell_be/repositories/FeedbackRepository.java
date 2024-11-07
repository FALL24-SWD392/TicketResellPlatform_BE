package com.swd392.ticket_resell_be.repositories;


import com.swd392.ticket_resell_be.entities.Feedback;
import com.swd392.ticket_resell_be.entities.Order;
import com.swd392.ticket_resell_be.enums.Categorize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
    Feedback findByOrder(Order order);

    Feedback findFeedbackByIdIs(UUID id);

    Page<Feedback> findAllByOrderIdAndStatus(UUID id, Categorize status, Pageable page);
}
