package com.nightluxe.core.dto.response;




import java.util.List;


public record CategoryResponseDTO(
        Long id,
        String name,
        String slug,
        String description,
        boolean requiresCredit,
        List<CategoryResponseDTO> subCategories) {
}
