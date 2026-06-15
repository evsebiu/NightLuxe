package com.nightluxe.core.dto.request;

public record AdSearchCriteriaDTO (
        String keyword,
        Long categoryId,
        Integer minPrice,
        Integer maxPrice,
        String location
){
}
