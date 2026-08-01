package com.nightluxe.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nightluxe.core.dto.request.CryptoPaymentRequestDTO;
import com.nightluxe.core.dto.request.CryptoWebHookPayloadDTO;
import com.nightluxe.core.dto.response.CryptoPaymentResponseDTO;
import com.nightluxe.core.entity.User;
import com.nightluxe.core.service.PaymentService;
import com.nightluxe.core.service.PaymentWebHookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentWebHookService webHookService;
    private final ObjectMapper objectMapper;
    private final PaymentService paymentService;

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

    @PostMapping("/crypto/init")
    public ResponseEntity<CryptoPaymentResponseDTO> initPayment(
            @Valid @RequestBody CryptoPaymentRequestDTO request,
            @AuthenticationPrincipal User currentUser){

        CryptoPaymentResponseDTO response = paymentService.initiateCryptoPayment(request, currentUser);

        return ResponseEntity.ok(response);
    }
}
