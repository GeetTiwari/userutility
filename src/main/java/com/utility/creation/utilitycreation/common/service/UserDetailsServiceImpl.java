package com.utility.creation.utilitycreation.common.service;


import com.utility.creation.utilitycreation.model.entity.UserRoleMapping;
import com.utility.creation.utilitycreation.repository.RoleRepository;
import com.utility.creation.utilitycreation.repository.UserRoleMappingRepository;
import com.utility.creation.utilitycreation.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.utility.creation.utilitycreation.exceptions.customexception.CustomUsernameNotFoundException;
import com.utility.creation.utilitycreation.model.entity.User;
import com.utility.creation.utilitycreation.repository.UserRepo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private UserRoleMappingRepository userRoleMappingRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByEmail(username);
        if (user != null) {

            List<String> roles=getRolesById(user.getId());

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(roles.toArray(new String[0]))
                    .build();
        }
        throw new CustomUsernameNotFoundException("User not found with username: " + username +" / Invalid Token");
    }

    public List<String> getRolesById(String userId) {
        List<UserRoleMapping> userRoleMappings = userRoleMappingRepository.findAll().stream()
                .filter(mapping -> userId.equals(mapping.getId()))
                .toList();

        return userRoleMappings.stream()
                .map(mapping -> roleRepository.findByRoleId(mapping.getRoleId()).getRoleName())
                .toList();
    }
}

