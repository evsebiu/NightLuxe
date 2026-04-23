package com.nightluxe.core.repository;

import com.nightluxe.core.entity.AdReport;
import com.nightluxe.enums.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdReportRepository extends JpaRepository<AdReport, Long> {
    List<AdReport> findByReason(String reason);
    List<AdReport> findByStatus(ReportStatus status);
    Optional<AdReport> findByAdvertisementId(Long advertisementId);
}
