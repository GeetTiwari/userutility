package com.utility.creation.utilitycreation.common.service;

import com.utility.creation.utilitycreation.model.entity.UserRoleMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.utility.creation.utilitycreation.model.dto.UserDTO;
import com.utility.creation.utilitycreation.model.dto.UserResponseDTO;
import com.utility.creation.utilitycreation.model.entity.User;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "createdTime", target = "createdTime")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "modifiedTime", target = "modifiedTime")
    UserDTO userToUserDTO(User user);
    User userDTOToUser(UserDTO userDTO);
    UserResponseDTO userToUserResponseDTO(User user);
}
