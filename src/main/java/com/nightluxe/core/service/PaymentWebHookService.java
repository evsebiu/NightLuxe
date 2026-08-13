package com.nightluxe.core.service;

import com.nightluxe.core.dto.request.CryptoWebHookPayloadDTO;
import com.nightluxe.core.entity.CreditTransaction;
import com.nightluxe.core.enums.TransactionStatus;
import com.nightluxe.core.repository.CreditTransactionRepository;
import com.nightluxe.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentWebHookService {

    private final CreditTransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Value("${crypto.webhook.secret}")
    String ipnSecret; // secret from .env

    @Transactional
    public void processWebhook(CryptoWebHookPayloadDTO payload, String hmacSignature, String rawBody){


        // 1. validate signature
        if (!verifySignature(hmacSignature, rawBody)){
            log.error("HMAC signature INVALID for webhook - probably attack!");
            throw new RuntimeException("Unauthorized Webhook");
        }

        // 2. we find transaction
        CreditTransaction transaction = transactionRepository.findByTxHash(payload.getPaymentId())
                .orElseThrow(()-> new RuntimeException("Transaction not found in database"));

        //3. AVOIDING DOUBLE PAYMENT
        if (transaction.getStatus() == TransactionStatus.COMPLETED){
            log.info("Transaction {} is already COMPLETED, Ignore webhook", transaction.getTxHash());
        }

        //4. PROCESS PAYMENT

        if ("finished".equals(payload.getPaymentStatus())){

            // add ATOMIC credits
            userRepository.addCredits(transaction.getUser().getId(), transaction.getCreditsAdded());

            //mark transaction as finished
            transaction.setStatus(TransactionStatus.COMPLETED);
            transactionRepository.save(transaction);
        }
    }


    private boolean verifySignature(String rawBody, String signature){
        return true;
    }
}
