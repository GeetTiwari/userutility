package com.utility.creation.utilitycreation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.utility.creation.utilitycreation.model.dto.UserDTO;
import com.utility.creation.utilitycreation.model.dto.UserResponseDTO;
import com.utility.creation.utilitycreation.service.RegisterService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class RegisterController {
    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(registerService.saveUser(userDTO));
    }

    @GetMapping("/verify")
    public ResponseEntity<Object> verify(@RequestParam String token) {
    return ResponseEntity.ok(registerService.verifyUser(token));
    }
}
