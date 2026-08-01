package com.nightluxe.core.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CryptoPaymentResponseDTO(
        String txHash, // transaction ID for process
        String payAddress, // USDT address generated for user
        BigDecimal payAmount, // Amount  to pay
        String payCurrency // ex : "USDTTRC20"
){}

