package com.utility.creation.utilitycreation.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utility.creation.utilitycreation.model.dto.LoginRequestDTO;
import com.utility.creation.utilitycreation.model.dto.LoginResponseDTO;
import com.utility.creation.utilitycreation.model.dto.RefreshTokenRequest;
import com.utility.creation.utilitycreation.model.response.AuthResponse;
import com.utility.creation.utilitycreation.service.LoginService;
import com.utility.creation.utilitycreation.service.RefreshTokenService;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/auth")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private RefreshTokenService refreshTokenService;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = loginService.login(loginRequest);
        if (response.getAccessToken() != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshTokenRequest refreshToken) {
        AuthResponse authResponse= refreshTokenService.refreshToken(refreshToken.getRefreshToken());
        if (authResponse == null) {
            return ResponseEntity.status(403).body(null);
        } else {
            return ResponseEntity.ok(authResponse);
        }
    }

    @PostMapping("/logout")public ResponseEntity<String> logout(@RequestBody RefreshTokenRequest refreshToken) {
        refreshTokenService.deleteRefreshToken(refreshToken.getRefreshToken());
        return ResponseEntity.ok("Logged out successfully");
    }
}