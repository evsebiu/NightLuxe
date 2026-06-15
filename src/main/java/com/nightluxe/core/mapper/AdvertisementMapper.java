package com.nightluxe.core.mapper;

import com.nightluxe.core.dto.response.AdvertisementResponseDTO;
import com.nightluxe.core.entity.AdImage;
import com.nightluxe.core.entity.Advertisement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdvertisementMapper {

    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "images", target = "imageUrl")
    AdvertisementResponseDTO toResponseDTO(Advertisement advertisement);

    default List<String> mapImagesToUrl(List<AdImage> images){
        if (images == null){
            return null;
        }
        return images.stream()
                .map(AdImage::getImageUrl)
                .toList();
    }
}
