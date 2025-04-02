package com.utility.creation.utilitycreation.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.utility.creation.utilitycreation.model.entity.Roles;



@Repository
public interface RoleRepository extends CrudRepository<Roles, String> {
    Roles findByRoleName(String roleName);
    Roles findByRoleId(int roleId);
}