package com.swd392.ticket_resell_be.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "order_details")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private Order order;

    @NotEmpty
    @Length(max = 50)
    @Column(name = "ticket_title", nullable = false, updatable = false, length = 50)
    private String ticketTitle;

    @Positive
    @Column(name = "quantity", nullable = false, updatable = false)
    private int quantity;
}