package com.nightluxe.core.dto.response;

import java.time.Instant;
import java.util.List;

public record AdvertisementResponseDTO(
        Long id,
        String title,
        String description,
        Integer price,
        String location,
        String categoryName,
        Integer viewCount,
        Integer phoneRevealsCount,
        Instant createdAt,
        Instant expiresAt,
        List<String> imageUrl ) {
}
