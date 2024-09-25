package com.swd392.ticket_resell_be.entities;

import com.swd392.ticket_resell_be.enums.Categorize;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "username", nullable = false, unique = true, updatable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, updatable = false)
    private Categorize role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Categorize status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_register", nullable = false, updatable = false)
    private Categorize typeRegister;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "rating", nullable = false)
    private float rating = 0;

    @Column(name = "reputation", nullable = false)
    private int reputation = 100;

}