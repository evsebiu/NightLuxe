package com.nightluxe.core.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PromotionRequestDTO(
        @NotBlank (message = "Type of promotion is required!")
        String promotionType // MIGHT BE "TOP_AD_7_DAYS", "HIGHTLIGHT" "BUMP"
){}
