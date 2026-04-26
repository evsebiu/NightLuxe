package com.nightluxe.core.mapper;


import com.nightluxe.core.entity.User;
import com.nightluxe.dto.request.RegisterRequestDTO;
import com.nightluxe.dto.response.UserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {


    @Mapping(target = "passwordHash", ignore = true)
    User toEntity(RegisterRequestDTO requestDTO);

    UserResponseDTO toResponseDTO(User user);
}
