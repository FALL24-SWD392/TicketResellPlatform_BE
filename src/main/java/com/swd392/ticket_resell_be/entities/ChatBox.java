package com.swd392.ticket_resell_be.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "chat_boxs")
@EntityListeners(AuditingEntityListener.class)
public class ChatBox {
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(name = "chat_id", nullable = false, updatable = false)
    private String chatId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private User sender;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private User recipient;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private Ticket ticket;

    @CreatedDate
    @NotNull
    @PastOrPresent
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Builder
    public ChatBox(String chatId, User sender, User recipient) {
        this.chatId = chatId;
        this.sender = sender;
        this.recipient = recipient;
    }

    public ChatBox() {
    }
}
