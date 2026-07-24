package com.nightluxe.core.service;

import com.nightluxe.core.entity.Advertisement;
import com.nightluxe.core.entity.AutoRepostRule;
import com.nightluxe.core.repository.AdvertisementRepository;
import com.nightluxe.core.repository.AutoRepostRuleRepository;
import com.nightluxe.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

public class AutoRepostCronService {

    private final AutoRepostRuleRepository autoRepostRuleRepository;
    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;

    private static final int BUMP_COST = 5; // standard cost for an optimezed BUMP;

    @Transactional
    public void processAutoReposts(){

        // 1. we get current hour  HH:00
        String currentHour = LocalTime.now(ZoneId.of("Europe/Malta")).format(DateTimeFormatter.ofPattern("HH:00"));

        log.info("Start auto-repost for hour: {}");

        List<AutoRepostRule> rulesToProcess = autoRepostRuleRepository.findActiveRulesForHour(currentHour);

        for (AutoRepostRule rule: rulesToProcess){
            Advertisement ad = rule.getAdvertisement();
            Long userId = ad.getUser().getId();

            // try to extract atomic credits
            int rowsUpdated = userRepository.deductCredits(userId, BUMP_COST);

            if (rowsUpdated == 1){
                // user had funds ->  we do BUMP
                ad.setCreatedAt(Instant.now());
                advertisementRepository.save(ad);
                log.debug("Auto-bump positive for ad ID: {}");
            } else {
                // user doesn't have credits, we stop the rule.
            } rule.setIsActive(false);
            autoRepostRuleRepository.save(rule);
            log.warn("Auto-bump failed (insufficient funds) for user ID: {}. Rule deactivated ", userId);
        }

        log.info("Auto-bump completed for hour: {}", currentHour);
    }

}
