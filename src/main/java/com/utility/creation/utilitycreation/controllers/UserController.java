package com.utility.creation.utilitycreation.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.utility.creation.utilitycreation.common.service.UserMapper;
import com.utility.creation.utilitycreation.model.dto.UserDTO;
import com.utility.creation.utilitycreation.model.dto.UserResponseDTO;
import com.utility.creation.utilitycreation.model.dto.UserRoleMappingDTO;
import com.utility.creation.utilitycreation.model.entity.User;
import com.utility.creation.utilitycreation.service.UserService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService) {
        this.userService = userService;
        this.userMapper = UserMapper.INSTANCE;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable String id) {
        UserResponseDTO user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/roles")
    public List<UserRoleMappingDTO> getAllUsersWithRoles() {
        return userService.getAllUsersWithRoles();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable("id") String id, @Valid @RequestBody UserDTO userDTO) {
        User user = userMapper.userDTOToUser(userDTO);
        try {
            // Call the service layer to update the user
            UserResponseDTO updatedUserDTO = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUserDTO);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(404).body(null); // User not found
        }
    }

    @PutMapping("/{userId}/update-role")
    public ResponseEntity<?> updateUserRole(@PathVariable int userId, @RequestParam String newRoleName) {
        try {
            UserRoleMappingDTO responseDTO = userService.updateUserRole(userId, newRoleName);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        String responseMessage = userService.deleteUser(id);
        return ResponseEntity.ok(responseMessage);
    }

    @DeleteMapping("/{userId}/role")
    public ResponseEntity<String> deleteUserRole(@PathVariable String userId) {
        String response = userService.deleteUserRole(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}/assign-roles")
    public ResponseEntity<String> assignRolesToUser(@PathVariable String userId, @RequestBody List<String> roleNames) {
        try {
            userService.assignRolesToUser(userId, roleNames);
            return ResponseEntity.ok("Roles assigned successfully.");
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{userId}/roles")
    public ResponseEntity<String> removeRolesFromUser(
            @PathVariable String userId,
            @RequestBody List<Integer> roleIds) {

        String responseMessage = userService.removeRolesFromUser(userId, roleIds);
        return ResponseEntity.ok(responseMessage);
    }
}
