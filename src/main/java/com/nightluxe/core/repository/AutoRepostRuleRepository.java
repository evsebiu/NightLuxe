package com.nightluxe.core.repository;

import com.nightluxe.core.entity.AutoRepostRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutoRepostRuleRepository extends JpaRepository<AutoRepostRule, Long> {

    List<AutoRepostRule> findByIsActiveTrue();

    @Query("SELECT r FROM AutoRepostRule r JOIN r.scheduledHours h WHERE r.isActive = true AND h = :currentHour")
    List<AutoRepostRule> findActiveRulesForHour(@Param("currentHour") String currentHour);
}
