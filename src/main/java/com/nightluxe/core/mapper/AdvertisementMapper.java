package com.nightluxe.core.mapper;

import com.nightluxe.core.dto.response.AdvertisementResponseDTO;
import com.nightluxe.core.entity.Advertisement;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdvertisementMapper {

    AdvertisementResponseDTO toResponseDTO(Advertisement advertisement);
}
