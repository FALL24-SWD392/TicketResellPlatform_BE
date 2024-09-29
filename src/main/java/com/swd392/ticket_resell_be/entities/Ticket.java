package com.swd392.ticket_resell_be.entities;

import com.swd392.ticket_resell_be.enums.Categorize;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private User seller;

    @NotEmpty
    @Length(max = 50)
    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "image")
    private String image;

    @Future
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "exp_date", nullable = false)
    private Date expDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 25)
    private Categorize type;

    @Column(name = "unit_price", nullable = false)
    private float unitPrice;

    @Positive
    @Column(name = "quantity", nullable = false)
    private int quantity;

    @NotNull
    @Length(max = 500)
    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 15)
    private Categorize status;

    @CreatedDate
    @NotNull
    @PastOrPresent
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedBy
    @NotBlank
    @Length(max = 50)
    @Column(name = "updated_by", nullable = false, length = 50)
    private String updatedBy;

    @LastModifiedDate
    @NotNull
    @PastOrPresent
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;
}