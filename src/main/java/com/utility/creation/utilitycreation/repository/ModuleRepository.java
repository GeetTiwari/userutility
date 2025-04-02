package com.utility.creation.utilitycreation.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.utility.creation.utilitycreation.model.entity.Modules;


@Repository
public interface ModuleRepository extends CrudRepository<Modules, String> {
    
}
