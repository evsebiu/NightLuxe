package com.nightluxe.core.mapper;


import com.nightluxe.core.dto.response.CategoryResponseDTO;
import com.nightluxe.core.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    CategoryResponseDTO toResponseDTO(Category category);
}
