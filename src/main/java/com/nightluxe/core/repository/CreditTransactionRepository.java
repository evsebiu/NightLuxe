package com.nightluxe.core.repository;

import com.nightluxe.core.entity.CreditTransaction;
import com.nightluxe.enums.TransactionStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CreditTransactionRepository extends JpaRepository<CreditTransaction, Long> {

    List<CreditTransaction> findByCreditsAdded(Integer creditsAdded);
    Optional<CreditTransaction> findByTxHash(String txHash);
    List<CreditTransaction> findByAmount(BigDecimal amount);
    Page<CreditTransaction> findByStatus(TransactionStatus status, Pageable pageable);
    Page<CreditTransaction> findByUserId(Long userId, Pageable pageable);
}
