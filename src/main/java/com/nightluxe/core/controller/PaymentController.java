package com.nightluxe.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nightluxe.core.dto.request.CryptoWebHookPayloadDTO;
import com.nightluxe.core.service.PaymentWebHookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentWebHookService webHookService;
    private final ObjectMapper objectMapper;

    @PostMapping("/crypto/webook")
    public ResponseEntity<String> handleCryptoWebhook(
            @RequestHeader(value = "x-pay-signature" ,required = false) String signature,
                @RequestBody String rawBody){

        try{
            CryptoWebHookPayloadDTO payload = objectMapper.readValue(rawBody, CryptoWebHookPayloadDTO.class);

            webHookService.processWebhook(payload, signature, rawBody);

            return ResponseEntity.ok("OK");
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Error processing webhook");
        }
    }
}
