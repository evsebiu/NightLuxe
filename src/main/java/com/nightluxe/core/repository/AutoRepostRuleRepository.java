package com.nightluxe.core.repository;

import com.nightluxe.core.entity.AutoRepostRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutoRepostRuleRepository extends JpaRepository<AutoRepostRule, Long> {

    List<AutoRepostRule> findByIsActiveTrue();
}
