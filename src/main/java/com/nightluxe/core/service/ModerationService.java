package com.nightluxe.core.service;

import com.nightluxe.core.dto.request.ReportRequestDTO;
import com.nightluxe.core.entity.AdReport;
import com.nightluxe.core.entity.Advertisement;
import com.nightluxe.core.enums.AdStatus;
import com.nightluxe.core.enums.ReportStatus;
import com.nightluxe.core.exceptions.BadRequestException;
import com.nightluxe.core.repository.AdReportRepository;
import com.nightluxe.core.repository.AdvertisementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModerationService {

    private final AdReportRepository adReportRepository;
    private final AdvertisementRepository advertisementRepository;

    private static final int AUTO_SUSPEND_THRESHOLD = 5;

    @Transactional
    public void reportAdvertisement(Long adId, ReportRequestDTO request) {
        Advertisement ad = advertisementRepository.findById(adId)
                .orElseThrow(() -> new BadRequestException("Ad cannot be found"));

        // 1. we populate entity
        AdReport report = new AdReport();
        report.setReason(request.reason());
        report.setAdvertisement(ad);

        // we set manual values of initilization
        report.setCreatedAt(Instant.now());
        report.setReportStatus(ReportStatus.REPORT_OPEN); // we use ENUM class

        advertisementRepository.save(ad);
        log.info("New report added for AD ID: {}", adId);

        // 2. check for maximum limit

        long openReportsCount = adReportRepository.countByAdvertisementIdAndReportStatus(adId, ReportStatus.REPORT_OPEN);

        if (openReportsCount >= AUTO_SUSPEND_THRESHOLD && ad.getStatus() == AdStatus.ACTIVE ){

            ad.setStatus(AdStatus.PENDING);
            advertisementRepository.save(ad);

            log.warn("Ad ID: {} was suspended because it got {} reports", adId, openReportsCount);
        }

    }
}
