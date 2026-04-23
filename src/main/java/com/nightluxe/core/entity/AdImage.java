package com.nightluxe.core.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ad_images")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class AdImage {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    private Boolean isCover = false; // false value for default

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id", nullable = false)
    private Advertisement advertisement;

}
