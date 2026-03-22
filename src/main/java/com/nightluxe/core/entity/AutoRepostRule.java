package com.nightluxe.core.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "repost_rule")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class AutoRepostRule {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private List<LocalTime> scheduledHours;

    private boolean isActive = true;


}
