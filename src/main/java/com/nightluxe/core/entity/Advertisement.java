package com.nightluxe.core.entity;


import com.nightluxe.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "advertisements")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Advertisement {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @NotNull
    private Integer price;

    @NotNull
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private Integer viewCount;

    private Integer phoneRevealsCount;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

}
