package com.utility.creation.utilitycreation.common.service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.utility.creation.utilitycreation.model.dto.LoginRequestDTO;
import com.utility.creation.utilitycreation.model.dto.LoginResponseDTO;
import com.utility.creation.utilitycreation.model.entity.User;
@Mapper
public interface LoginMapper {
LoginMapper INSTANCE = Mappers.getMapper(LoginMapper.class);
@Mapping(target = "email", source = "email")  // Explicit mapping
@Mapping(target = "password", source = "password")
@Mapping(target = "createdDate", ignore = true)
@Mapping(target = "createdTime", ignore = true)
@Mapping(target = "modifiedDate", ignore = true)
@Mapping(target = "modifiedTime", ignore = true)
@Mapping(target = "id", ignore = true)
@Mapping(target = "firstName", ignore = true)
@Mapping(target = "lastName", ignore = true)
@Mapping(target = "phone", ignore = true)
@Mapping(target = "verified", ignore = true)
User loginRequestDTOToUser(LoginRequestDTO loginRequestDTO);
    @Mapping(target = "message", ignore=true)
    @Mapping(target = "accessToken", ignore=true)
    @Mapping(target = "refreshToken", ignore=true)
    LoginResponseDTO userToLoginResponseDTO(User user);
}