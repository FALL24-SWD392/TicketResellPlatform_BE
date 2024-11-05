package com.swd392.ticket_resell_be.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "chat_notifications")
@EntityListeners(AuditingEntityListener.class)
public class ChatNotification {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private User sender;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private User recipient;

    @NotNull
    @Column(name = "content", nullable = false, updatable = false)
    private String content;
}
