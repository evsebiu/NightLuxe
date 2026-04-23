package com.nightluxe.core.entity;


import com.nightluxe.enums.AdStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "advertisements")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Advertisement {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT" ,nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdStatus status;

    private Integer viewCount = 0;

    private Integer phoneRevealsCount = 0;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;


    @PrePersist
    protected void onCreate(){
        createdAt = LocalDateTime.now();
        expiresAt = LocalDateTime.now().plusDays(30L);

        // set a default status
        if (status == null){
            status = AdStatus.PENDING;
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdImage> images = new ArrayList<>();
}
