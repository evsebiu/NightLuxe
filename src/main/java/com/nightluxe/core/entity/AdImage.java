package com.nightluxe.core.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "images")
public class AdImage {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    private Boolean isCover;

}
