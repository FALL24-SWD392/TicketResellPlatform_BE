package com.swd392.ticket_resell_be.entities;

import com.swd392.ticket_resell_be.enums.Categorize;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"Report\"")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "report_id", nullable = false)
    private UUID id;

    @NotNull
    @Column(name = "reporter_id", nullable = false)
    private UUID reporterId;

    @NotNull
    @Column(name = "reported_id", nullable = false)
    private UUID reportedId;

    @NotNull
    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @ColumnDefault("'pending'")
    @Column(name = "status", columnDefinition = "status")
    private Categorize status;

    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @NotNull
    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private LocalDate updatedAt;
}