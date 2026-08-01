package com.nightluxe.core.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.nightluxe.core.dto.request.CryptoPaymentRequestDTO;
import com.nightluxe.core.dto.response.CryptoPaymentResponseDTO;
import com.nightluxe.core.entity.CreditTransaction;
import com.nightluxe.core.entity.User;
import com.nightluxe.core.enums.TransactionStatus;
import com.nightluxe.core.repository.CreditTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j

public class PaymentService {

    private final CreditTransactionRepository transactionRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${crypto.api.key}")  // key added on application.properties or .env
    private String apiKey;

    private static final String NOWPAYMENTS_API_URL = "https://api.nowpayments.io/v1/payment";
    private static final int CREDITS_PER_USD = 10; // conversion rate (example : 1 USDT= 10 credits)


    @Transactional
    public CryptoPaymentResponseDTO initiateCryptoPayment (CryptoPaymentRequestDTO request, User currentUser) {


        // 1. calculate credits to be added after confirmation
        int creditsToAdd  = request.usdAmount().intValue() * CREDITS_PER_USD;

        // 2. we build payload for NowPayments
        Map<String, Object> paymentPayload = Map.of(
                "price_amount", request.usdAmount(),
                "price_currency", "usd",
                "pay_currency", "usdttrc20", // TRON network for lower fees
                "orderd_id", "USER_ " + currentUser.getId() + "_" + System.currentTimeMillis()
        );

        // 3. we call external API using Webclient
        log.info("Initiating payment to external processor for USER ID: {}", currentUser);

        JsonNode responseNode = webClientBuilder.build()
                .post()
                .uri(NOWPAYMENTS_API_URL)
                .header("x-api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(paymentPayload)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block(); // we use .block() because we need immediate results to save in database

        if (responseNode == null || !responseNode.has("payment_id")){
            log.error("Error generating crypto payment");
            throw new RuntimeException("We didn't could generate address for payment. Try again");
        }

        // 4. Extract data from processor response

        String txHash = responseNode.get("payment_id").asText();
        String payAddress = responseNode.get("pay_address").asText();
        BigDecimal payAmount = new BigDecimal(responseNode.get("pay_amount").asText());
        String payCurrency = responseNode.get("payment_currency").asText();


        // 5. we save transaction in our database for PENDING status

        CreditTransaction transaction = new CreditTransaction();
        transaction.setAmount(request.usdAmount());
        transaction.setCreditsAdded(creditsToAdd);
        transaction.setCryptoWalletAddress(payAddress);
        transaction.setTxHash(txHash);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setUser(currentUser);

        transactionRepository.save(transaction);

        log.info("Transaction PENDING saved as success. TX HASH: {} ", txHash);

        // 6. return DTO to client to shiw him address and QR CODE
        return CryptoPaymentResponseDTO.builder()
                .txHash(txHash)
                .payAddress(payAddress)
                .payAmount(payAmount)
                .payCurrency(payCurrency)
                .build();

    }

}
