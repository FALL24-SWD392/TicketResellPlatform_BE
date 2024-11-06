package com.swd392.ticket_resell_be.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @NotEmpty
    @Length(max = 50)
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Positive
    @Column(name = "sale_limit", nullable = false)
    private int saleLimit;

    @NotEmpty
    @Length(max = 500)
    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @PositiveOrZero
    @Column(name = "point_required", nullable = false)
    private int pointRequired;

    @Column(name = "price", nullable = false)
    private float price;

}