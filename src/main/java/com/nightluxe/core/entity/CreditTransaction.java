package com.nightluxe.core.entity;


import com.nightluxe.core.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "credit_transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CreditTransaction {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private Integer creditsAdded;

    @Column(nullable = false)
    private String cryptoWalletAddress;

    @Column(unique = true)
    private String txHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate(){
        createdAt = Instant.now();
        if (status == null) {
            status=TransactionStatus.PENDING;
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
