package com.nightluxe.core.entity;


import com.nightluxe.enums.Payment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "credit_transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class CreditTransaction {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer amount; // USDT

    @Column(nullable = false)
    private Integer creditsAdded;

    @Column(nullable = false)
    private String cryptoWalletAddress;

    @Column(nullable = false)
    private String txHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Payment payment;

    private LocalDateTime createdAt;


}
