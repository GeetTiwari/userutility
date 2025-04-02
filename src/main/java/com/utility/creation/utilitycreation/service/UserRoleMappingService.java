package com.utility.creation.utilitycreation.service;

import com.utility.creation.utilitycreation.model.entity.UserRoleMapping;
import com.utility.creation.utilitycreation.repository.UserRoleMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleMappingService {
    @Autowired
    UserRoleMappingRepository userRoleMappingRepository;

    // Method to delete user-role mappings
    public void deleteMappingsByUserId(String userId) {
        List<UserRoleMapping> allMappings = userRoleMappingRepository.findAll();

        // Filter mappings that belong to the given user ID
        List<UserRoleMapping> userMappings = allMappings.stream().filter(mapping -> mapping.getId().equals(userId)).toList();

        if (userMappings.isEmpty()) {
            System.out.println("No mappings found for userId: " + userId);
            return;
        }

        // Delete each mapping
        for (UserRoleMapping mapping : userMappings) {
            userRoleMappingRepository.delete(mapping);
        }
    }
}