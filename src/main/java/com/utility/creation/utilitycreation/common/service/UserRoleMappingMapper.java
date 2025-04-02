package com.utility.creation.utilitycreation.common.service;

import java.util.Collections;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.utility.creation.utilitycreation.model.dto.UserRoleMappingDTO;
import com.utility.creation.utilitycreation.model.entity.UserRoleMapping;
@Mapper(componentModel = "spring")
public interface UserRoleMappingMapper {
    UserRoleMappingMapper INSTANCE = Mappers.getMapper(UserRoleMappingMapper.class);

    @Mapping(target = "roleNames", source = "roleId", qualifiedByName = "mapRoleIdToRoleNames")
    UserRoleMappingDTO userRoleMappingToUserRoleMappingDTO(UserRoleMapping userRoleMapping);
    UserRoleMapping userRoleMappingDTOToUserRoleMapping(UserRoleMappingDTO userRoleMappingDTO);

    @Named("mapRoleIdToRoleNames")
    default List<String> mapRoleIdToRoleNames(Integer roleId) {
        return Collections.singletonList("ROLE_USER"); // Placeholder, will override in service
    }
}
