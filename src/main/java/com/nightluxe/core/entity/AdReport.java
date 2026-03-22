package com.nightluxe.core.entity;


import com.nightluxe.enums.Report;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ad_report")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class AdReport {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Report report;

    private LocalDateTime createdAt;
}
