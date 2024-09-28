package com.swd392.ticket_resell_be.repositories;


import com.swd392.ticket_resell_be.entities.Report;
import com.swd392.ticket_resell_be.enums.Categorize;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface ReportRepository extends JpaRepository<Report, UUID> {
    List<Report> findReportByIdAndStatus(UUID id, Categorize status);

    Report findReportByIdIs(UUID id);

    List<Report> findAllByStatus(Categorize status);
}
