package com.nightluxe.core.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CryptoPaymentRequestDTO(
        @NotNull(message = "Amount cannot be null")
        @Min(value = 5, message = "Deposit amount must be 5 USDT")
        BigDecimal usdAmount
) {}
