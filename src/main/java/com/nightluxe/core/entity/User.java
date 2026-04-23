package com.nightluxe.core.entity;


import com.nightluxe.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String email;


    @Column(unique = true)
    @Pattern(regexp = "^(\\+356)?(2|7|9)\\d{7}$", message = "Please update to a valid format for Malta - +356")
    private String phoneNumber;

    @NotNull
    private LocalDate birthDate;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Integer creditBalance = 0; // default value

    private Boolean isVerified = false;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
        if (role == null){
            role = Role.ROLE_USER;
        }
    }
}
