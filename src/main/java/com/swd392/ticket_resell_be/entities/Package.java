package com.swd392.ticket_resell_be.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "\"Packages\"")
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "package_id", nullable = false)
    private UUID id;

    @Size(max = 50)
    @NotNull
    @Column(name = "package_name", nullable = false, length = 50)
    private String packageName;

    @Column(name = "sale_limit")
    private Integer saleLimit;

    @NotNull
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "image_urls")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> imageUrls;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @NotNull
    @Column(name = "status", nullable = false)
    @ColumnDefault("true")
    private boolean status;

}