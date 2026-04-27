package com.nightluxe.core.entity;


import com.nightluxe.core.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String email;


    @Column(unique = true)
    //@Pattern(regexp = "^(\\+356)?(2|7|9)\\d{7}$", message = "Please update to a valid format for Malta - +356")
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

    private Instant createdAt;

    @PrePersist
    protected void onCreate(){
        createdAt = Instant.now();
        if (role == null){
            role = Role.ROLE_USER;
        }
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        // phase 1, all users get a default role
        // later i will add other roles, such as ADMIN/MODERATOR
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword(){
        return this.passwordHash;
    }

    @Override
    public String getUsername(){
        return this.getUsername();
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return  true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }
}
