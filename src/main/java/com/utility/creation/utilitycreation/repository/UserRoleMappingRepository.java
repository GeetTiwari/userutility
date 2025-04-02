package com.utility.creation.utilitycreation.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.utility.creation.utilitycreation.model.entity.UserRoleMapping;

import java.util.List;

@Repository
public interface UserRoleMappingRepository extends CrudRepository<UserRoleMapping, String> {
    List<UserRoleMapping> findByMid(String mid);
    List<UserRoleMapping> findAll();
    List<UserRoleMapping> findByUserId(String userId);
}