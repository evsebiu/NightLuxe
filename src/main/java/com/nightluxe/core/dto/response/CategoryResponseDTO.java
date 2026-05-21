package com.nightluxe.core.dto.response;


import lombok.Data;

@Data
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private String slug;
    private boolean requiresCredit;
}
