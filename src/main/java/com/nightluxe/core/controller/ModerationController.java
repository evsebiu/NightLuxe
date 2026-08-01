package com.nightluxe.core.controller;


import com.nightluxe.core.dto.request.ReportRequestDTO;
import com.nightluxe.core.service.ModerationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/moderation")
@RequiredArgsConstructor

public class ModerationController {

    private final ModerationService moderationService;

    @PostMapping
    public ResponseEntity<String> reportAd(
            @PathVariable Long adId,
            @Valid @RequestBody ReportRequestDTO request
            ){

        moderationService.reportAdvertisement(adId, request);

        return ResponseEntity.ok("Ad has been successfully reported");
    }
}
