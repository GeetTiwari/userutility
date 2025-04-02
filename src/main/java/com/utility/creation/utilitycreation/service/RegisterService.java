package com.utility.creation.utilitycreation.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.utility.creation.utilitycreation.common.service.UserMapper;
import com.utility.creation.utilitycreation.exceptions.customexception.ExistingEmailException;
import com.utility.creation.utilitycreation.exceptions.customexception.ResourceNotFoundException;
import com.utility.creation.utilitycreation.exceptions.customexception.TokenExpiredException;
import com.utility.creation.utilitycreation.exceptions.customexception.UserAlreadyVerifiedException;
import com.utility.creation.utilitycreation.exceptions.customexception.UserNotFoundException;
import com.utility.creation.utilitycreation.model.dto.UserDTO;
import com.utility.creation.utilitycreation.model.dto.UserResponseDTO;
import com.utility.creation.utilitycreation.model.entity.Roles;
import com.utility.creation.utilitycreation.model.entity.User;
import com.utility.creation.utilitycreation.model.entity.UserRoleMapping;
import com.utility.creation.utilitycreation.repository.RoleRepository;
import com.utility.creation.utilitycreation.repository.UserRepo;
import com.utility.creation.utilitycreation.repository.UserRoleMappingRepository;
import com.utility.creation.utilitycreation.utils.Constants;
import com.utility.creation.utilitycreation.utils.JwtUtil;

@Service
public class RegisterService {
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final UserRoleMappingRepository userRoleMappingRepository;

    @Value("${email.url.link}")
    private String activationLink;

    public RegisterService(UserRepo userRepo, RoleRepository roleRepository,
            UserRoleMappingRepository userRoleMappingRepository,
            EmailService emailService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepository = roleRepository;
        this.userRoleMappingRepository = userRoleMappingRepository;
        this.userMapper = UserMapper.INSTANCE;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO saveUser(UserDTO userDTO) {
        User user = userMapper.userDTOToUser(userDTO);
        User existingUser = userRepo.findByEmail(user.getEmail());

        if (existingUser != null) {
            throw new ExistingEmailException(Constants.EXISTING_EMAIL_ERROR);
        }
        Roles defaultRole = roleRepository.findByRoleName("USER");
        if (defaultRole == null) {
            throw new ResourceNotFoundException("Default role not found in the database!");
        }

        // Save the user
        user.setId(String.valueOf(Math.abs(UUID.randomUUID().hashCode())));
        user.setVerified(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepo.save(user);

        // Save the user-role mapping
        UserRoleMapping mapping = createRoleMapping(savedUser, defaultRole);
        userRoleMappingRepository.save(mapping);

        UserResponseDTO userResponseDTO = userMapper.userToUserResponseDTO(savedUser);

        // Generate JWT for verification
        String token = jwtUtil.createRegistrationToken(savedUser.getEmail());

        // Prepare dynamic data for email template
        Map<String, String> dynamicData = new HashMap<>();
        dynamicData.put("name", savedUser.getFirstName());
        dynamicData.put("email", savedUser.getEmail());
        dynamicData.put("subject", Constants.SUBJECT);
        dynamicData.put("activationLink", activationLink + token);

        // Return the saved user as a DTO
        emailService.sendEmail(userResponseDTO.getEmail(), Constants.SUBJECT, dynamicData);
        return userResponseDTO;
    }

    public UserRoleMapping createRoleMapping(User user, Roles role) {
        String roleMappingKey = "UserRoleMapping:mid:" + user.getId(); // Correct key format

        UserRoleMapping mapping = new UserRoleMapping();
        mapping.setMid(roleMappingKey); // Ensure the key is clean
        mapping.setId(user.getId());
        mapping.setRoleId(role.getRoleId());

        return mapping;
    }

    public Object verifyUser(String token) {
        Map<String, String> response = new HashMap<>();

        if (token != null) {

            if (!jwtUtil.validateToken(token)) {
                throw new TokenExpiredException("Token has expired.");
            }

            String username = jwtUtil.extractUsername(token);

            User user= userRepo.findByEmail(username);
            if (user == null) {
                throw new UserNotFoundException("User not found.");
            }
            if (user.isVerified()) {
                throw new UserAlreadyVerifiedException("User already verified.");
            }

            user.setVerified(true); // Assuming 'User' has a 'setVerified' method
            userRepo.save(user);
            response.put("email", username);
            response.put("message", username + " verified successfully.");
            response.put("verified", "true");
        } else {
            System.out.println("No matching token found.");
            throw new RuntimeException("Invalid token.");
        }
        return response;
    }

}
