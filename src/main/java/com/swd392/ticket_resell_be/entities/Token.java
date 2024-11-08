package com.swd392.ticket_resell_be.entities;

import com.swd392.ticket_resell_be.enums.Categorize;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tokens")
@EntityListeners(AuditingEntityListener.class)
public class Token {
    @Id
    @NotNull
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @NotNull
    @FutureOrPresent
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "exp_at", nullable = false, updatable = false)
    private Date expAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Categorize status;

    @NotBlank
    @CreatedBy
    @Length(max = 50)
    @Column(name = "created_by", nullable = false, updatable = false, length = 50)
    private String createdBy;

    @NotBlank
    @Length(max = 50)
    @LastModifiedBy
    @Column(name = "updated_by", nullable = false, length = 50)
    private String updatedBy;

}