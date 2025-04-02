package com.utility.creation.utilitycreation.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.utility.creation.utilitycreation.common.service.UserMapper;
import com.utility.creation.utilitycreation.common.service.UserRoleMappingMapper;
import com.utility.creation.utilitycreation.exceptions.customexception.ResourceNotFoundException;
import com.utility.creation.utilitycreation.exceptions.customexception.UserNotFoundException;
import com.utility.creation.utilitycreation.model.dto.UserResponseDTO;
import com.utility.creation.utilitycreation.model.dto.UserRoleMappingDTO;
import com.utility.creation.utilitycreation.model.entity.Roles;
import com.utility.creation.utilitycreation.model.entity.User;
import com.utility.creation.utilitycreation.model.entity.UserRoleMapping;
import com.utility.creation.utilitycreation.repository.RoleRepository;
import com.utility.creation.utilitycreation.repository.UserRepo;
import com.utility.creation.utilitycreation.repository.UserRoleMappingRepository;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final UserRoleMappingRepository userRoleMappingRepository;
    private final UserRoleMappingMapper userRoleMappingMapper;
    private final UserRoleMappingService userRoleMappingService;

    public UserService(UserRepo userRepo, RoleRepository roleRepository,
            UserRoleMappingRepository userRoleMappingRepository,
            com.utility.creation.utilitycreation.service.UserRoleMappingService userRoleMappingService) {
        this.userRepo = userRepo;
        this.roleRepository = roleRepository;
        this.userRoleMappingRepository = userRoleMappingRepository;
        this.userMapper = UserMapper.INSTANCE;
        this.userRoleMappingMapper = UserRoleMappingMapper.INSTANCE;
        this.userRoleMappingService = userRoleMappingService;
    }

    public UserResponseDTO getUserById(String id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        return userMapper.userToUserResponseDTO(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepo.findAll().forEach(users::add);
        List<UserResponseDTO> userResponseDTOs = new ArrayList<>();
        for (User user : users) {
            userResponseDTOs.add(userMapper.userToUserResponseDTO(user));
        }
        return userResponseDTOs;
    }

    public String deleteUser(String id) {
        // Check if user exists
        User existingUser = userRepo.findById(id).orElse(null);
        if (existingUser == null) {
            return "User not found with ID: " + id;
        }

        // Delete user-role mappings
        userRoleMappingService.deleteMappingsByUserId(id);

        // Delete the user
        userRepo.deleteById(id);

        return "User and associated role mappings deleted successfully for ID: " + id;
    }

    public UserResponseDTO updateUser(String id, User user) {
        // Check if the user exists
        User existingUser = userRepo.findById(id).orElse(null);
        if (existingUser == null) {
            throw new UserNotFoundException("User not found with ID: " + id);
        }

        // Update fields of the existing user with new values
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setPhone(user.getPhone());
        existingUser.setModifiedDate(user.getModifiedDate());
        existingUser.setModifiedTime(user.getModifiedTime());

        User updatedUser = userRepo.save(existingUser);
        UserRoleMapping userRoleMapping = userRoleMappingRepository.findById(id).orElse(null);
        // Return the updated user as a DTO
        return userMapper.userToUserResponseDTO(updatedUser);
    }

    public UserRoleMappingDTO updateUserRole(int userId, String newRoleName) {
        User user = userRepo.findById(String.valueOf(userId)).orElse(null);
        if (user == null) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }

        // Check if the new role exists
        Roles newRole = roleRepository.findByRoleName(newRoleName);
        if (newRole == null) {
            throw new ResourceNotFoundException("Role not found: " + newRoleName);
        }

        // Check and update the user's role mapping
        String userRoleMappingKey = "UserRoleMapping:mid:" + userId;
        UserRoleMapping existingMapping = userRoleMappingRepository.findById(userRoleMappingKey).orElse(null);
        if (existingMapping == null) {
            throw new ResourceNotFoundException("User role mapping not found for user ID: " + userId);
        }

        existingMapping.setRoleId(newRole.getRoleId());
        userRoleMappingRepository.save(existingMapping);

        // Convert updated UserRoleMapping to DTO and return
        return userRoleMappingMapper.userRoleMappingToUserRoleMappingDTO(existingMapping);
    }

    public List<UserRoleMappingDTO> getAllUsersWithRoles() {
        List<UserRoleMapping> userRoleMappings = userRoleMappingRepository.findAll();

        Map<String, UserRoleMappingDTO> userRoleMap = new HashMap<>();

        for (UserRoleMapping mapping : userRoleMappings) {
            String userId = mapping.getId(); // Assuming 'id' is the user identifier
            UserRoleMappingDTO dto = userRoleMap.getOrDefault(userId, new UserRoleMappingDTO());

            dto.setId(userId);
            dto.setMid(mapping.getMid());

            // Ensure roleIds list is initialized
            if (dto.getRoleIds() == null) {
                dto.setRoleIds(new ArrayList<>());
            }

            // Ensure roleNames list is initialized
            if (dto.getRoleNames() == null) {
                dto.setRoleNames(new ArrayList<>());
            }

            // Add roleId and roleName if not already present
            if (!dto.getRoleIds().contains(mapping.getRoleId())) {
                dto.getRoleIds().add(mapping.getRoleId());
            }
            String roleName = roleRepository.findById(String.valueOf(mapping.getRoleId()))
                    .map(Roles::getRoleName).orElse("UNKNOWN_ROLE");
            if (!dto.getRoleNames().contains(roleName)) {
                dto.getRoleNames().add(roleName);
            }

            userRoleMap.put(userId, dto);
        }

        return new ArrayList<>(userRoleMap.values());
    }

    public String deleteUserRole(String userId) {
        userRoleMappingService.deleteMappingsByUserId(userId);

        Roles defaultRole = roleRepository.findByRoleName("USER");
        if (defaultRole == null) {
            return "Default role not found!";
        }

        // Create a new UserRoleMapping for the default role
        UserRoleMapping defaultMapping = new UserRoleMapping();
        defaultMapping.setId(userId); // Set the userId
        defaultMapping.setRoleId(defaultRole.getRoleId()); // Set the "USER" roleId

        // Save the new mapping to assign the default role to the user
        userRoleMappingRepository.save(defaultMapping);
        return "Roles deleted and default role assigned successfully.";
    }

    // To assign multiple roles to a user
    public void assignRolesToUser(String userId, List<String> roleNames) {
        // Find the user
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // Assign the roles to the user
        for (String roleName : roleNames) {
            Roles role = roleRepository.findByRoleName(roleName);
            if (role != null) {
                // Create a mapping for each role
                UserRoleMapping mapping = new UserRoleMapping(userId, role.getRoleId());
                userRoleMappingRepository.save(mapping);
            } else {
                throw new ResourceNotFoundException("Role not found: " + roleName);
            }
        }
    }

    public String removeRolesFromUser(String userId, List<Integer> roleIds) {
        List<UserRoleMapping> userRoleMappings = userRoleMappingRepository.findAll().stream()
                .filter(mapping -> userId.equals(mapping.getId()) && roleIds.contains(mapping.getRoleId())).toList();

        if (userRoleMappings.isEmpty()) {
            throw new ResourceNotFoundException("No matching roles found for removal.");
        }

        userRoleMappingRepository.deleteAll(userRoleMappings);

        // Check if the user still has any roles assigned
        boolean hasRoles = userRoleMappingRepository.findAll().stream()
                .anyMatch(mapping -> userId.equals(mapping.getId()));

        // If no roles remain, assign the default role
        if (!hasRoles) {
            Roles defaultRole = roleRepository.findByRoleName("USER");
            if (defaultRole == null) {
                return "Roles removed, but default role not found!";
            }

            // Assign the default role
            UserRoleMapping defaultMapping = new UserRoleMapping();
            defaultMapping.setId(userId);
            defaultMapping.setRoleId(defaultRole.getRoleId());

            userRoleMappingRepository.save(defaultMapping);
            return "Roles removed. Default role assigned.";
        }
        return "Roles removed successfully for user " + userId;
    }
}