package com.swd392.ticket_resell_be.entities;

import com.swd392.ticket_resell_be.enums.TicketStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"Tickets\"")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ticket_id", nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Size(max = 255)
    @NotNull
    @Column(name = "ticket_name", nullable = false)
    private String ticketName;

    @NotNull
    @Column(name = "ticket_expiration", nullable = false)
    private LocalDate ticketExpiration;

    @Size(max = 50)
    @Column(name = "ticket_type", length = 50)
    private String ticketType;

    @Column(name = "sale_price", precision = 10, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "ticket_attachment")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> ticketAttachment;
    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @ColumnDefault("'pending'")
    @Column(name = "status", columnDefinition = "ticket_status")
    private TicketStatus status;
}