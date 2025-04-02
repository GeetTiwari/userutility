package com.utility.creation.utilitycreation.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.utility.creation.utilitycreation.model.entity.User;

@Repository
public interface UserRepo extends CrudRepository<User, String> {
    
    public User findByEmail(String email);
}