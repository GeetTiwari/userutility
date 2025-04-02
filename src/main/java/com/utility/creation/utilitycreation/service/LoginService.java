package com.utility.creation.utilitycreation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.utility.creation.utilitycreation.common.service.UserDetailsServiceImpl;
import com.utility.creation.utilitycreation.model.dto.LoginRequestDTO;
import com.utility.creation.utilitycreation.model.dto.LoginResponseDTO;
import com.utility.creation.utilitycreation.model.entity.User;
import com.utility.creation.utilitycreation.repository.UserRepo;
import com.utility.creation.utilitycreation.utils.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;
    
    @Autowired
    private UserRepo userRepo;

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                                                 loginRequest.getPassword()));
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            User user = userRepo.findByEmail(userDetails.getUsername());
            if(!user.isVerified()) {
                return new LoginResponseDTO("Login failed. User is not verified", null,null);
            }
            final String token = jwtUtil.generateToken(userDetails.getUsername());
            String refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername(),token);
            return new LoginResponseDTO("Login successful", token, refreshToken);
        }
        catch(Exception exception){
            log.error("Exception occured while create Authentication token", exception);
            return new LoginResponseDTO("Login failed.Incorrect user name or password", null,null);
        }
    }


}