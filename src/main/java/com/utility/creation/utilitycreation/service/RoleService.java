package com.utility.creation.utilitycreation.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utility.creation.utilitycreation.model.dto.RolesDto;
import com.utility.creation.utilitycreation.model.entity.Roles;
import com.utility.creation.utilitycreation.repository.RoleRepository;



@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public RolesDto addRoles(RolesDto rolesDto) {
        Roles roles = new Roles(rolesDto.getRoleId(), rolesDto.getRoleName());
        Roles savedRoles = roleRepository.save(roles);
        return new RolesDto(savedRoles.getRoleId(), savedRoles.getRoleName(),savedRoles.getCreatedDate(),savedRoles.getCreatedTime(),savedRoles.getModifiedDate(),savedRoles.getModifiedTime());
    }

    // run a fetch query in the Redis Database
    // to get a list of all the Roles
    public List<RolesDto> getAllRoles() {
        List<RolesDto> allRolesDto = new ArrayList<>();
        roleRepository.findAll()
                      .forEach(role -> allRolesDto.add(new RolesDto(role.getRoleId(), role.getRoleName(),role.getCreatedDate(),role.getCreatedTime(),role.getModifiedDate(),role.getModifiedTime())));
        return allRolesDto;
    }

     // fetch operation to get Roles using an ID
    public RolesDto getRolesById(int id) {
        Optional<Roles> optionalRoles = roleRepository.findById(String.valueOf(id));
        if (optionalRoles.isPresent()) {
            Roles roles = optionalRoles.get();
            return new RolesDto(roles.getRoleId(), roles.getRoleName(),roles.getCreatedDate(),roles.getCreatedTime(),roles.getModifiedDate(),roles.getModifiedTime());
        }
        return null;
    }

    // update operation to existing Roles using an ID
    public RolesDto updateRolesById(int id, 
                RolesDto newRolesDto) {
        Optional<Roles> existingRoles = roleRepository.findById(String.valueOf(id));

        if (existingRoles.isPresent()) {
            Roles updatedRoles = existingRoles.get();
            updatedRoles.setRoleName(newRolesDto.getRoleName());
            updatedRoles.setModifiedDate(LocalDate.now());
            updatedRoles.setModifiedTime(LocalTime.now());

            Roles savedRoles = roleRepository.save(updatedRoles);
            return new RolesDto(savedRoles.getRoleId(), savedRoles.getRoleName(),savedRoles.getCreatedDate(),savedRoles.getCreatedTime(),savedRoles.getModifiedDate(),savedRoles.getModifiedTime());
        }

        return null;
    }

    // delete the existing Roles
    public void deleteRolesById(int id) {
        roleRepository.deleteById(String.valueOf(id));
    }
}
