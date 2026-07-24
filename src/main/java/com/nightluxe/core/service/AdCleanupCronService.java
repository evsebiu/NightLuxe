package com.nightluxe.core.service;

import com.nightluxe.core.repository.AdvertisementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdCleanupCronService {

    private final AdvertisementRepository advertisementRepository;


    // it runs every minute 0 every hour 24/7
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void cleanupExpiredVIPAds(){
        log.info("Starting running cron job: Cleaning VIP expired ads");

        int updatedRows = advertisementRepository.clearExpiredPromotions(Instant.now());

        log.info("Cron job 100% completed. Ads found:{}", updatedRows);
    }

}
