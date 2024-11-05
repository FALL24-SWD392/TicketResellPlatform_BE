package com.swd392.ticket_resell_be.repositories;


import com.swd392.ticket_resell_be.entities.Report;
import com.swd392.ticket_resell_be.enums.Categorize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface ReportRepository extends JpaRepository<Report, UUID> {
    Page<Report> findReportById(UUID id, Pageable page);

    Report findReportByIdIs(UUID id);

    Page<Report> findAllByStatus(Categorize status, Pageable page);
}
