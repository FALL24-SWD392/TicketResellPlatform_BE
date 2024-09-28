package com.swd392.ticket_resell_be.repositories;


import com.swd392.ticket_resell_be.entities.Feedback;
import com.swd392.ticket_resell_be.enums.Categorize;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
    Feedback findFeedbackByIdIs(UUID id);

    List<Feedback> findAllByOrderIdAndStatus(UUID id, Categorize status);
}
