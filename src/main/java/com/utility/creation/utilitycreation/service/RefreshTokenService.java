package com.utility.creation.utilitycreation.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.utility.creation.utilitycreation.model.entity.RefreshToken;
import com.utility.creation.utilitycreation.model.response.AuthResponse;
import com.utility.creation.utilitycreation.repository.RefreshTokenRepository;
import com.utility.creation.utilitycreation.utils.JwtUtil;

@Service
public class RefreshTokenService {
   private final RefreshTokenRepository repository;
   private final JwtUtil jwtUtil;

    public RefreshTokenService(RefreshTokenRepository repository, JwtUtil jwtUtil) {
        this.repository = repository;
        this.jwtUtil = jwtUtil;
    }
    public String createRefreshToken(String username, String accessToken) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setEmail(username);
        refreshToken.setRefreshToken(jwtUtil.generateRefreshToken(username));
        refreshToken.setAccessToken(accessToken);
        repository.save(refreshToken);       
        return refreshToken.getRefreshToken();
    }
    public Optional<RefreshToken> getRefreshToken(String token) {
        return repository.findByRefreshToken(token);
    }

    public void deleteRefreshToken(String refreshToken) {
        repository.findByRefreshToken(refreshToken).ifPresent(repository::delete);
    }

    public AuthResponse refreshToken(String refreshToken) {
        boolean isExpired = !jwtUtil.validateToken(refreshToken);
        if(isExpired) {
            return new AuthResponse(null, null, "Invalid refresh token");
        }
        return getRefreshToken(refreshToken)
                .map(token -> {                
                    String newAccessToken = jwtUtil.generateToken(token.getEmail());
                    String newRefreshToken = createRefreshToken(token.getEmail(), newAccessToken);
                    deleteRefreshToken(refreshToken);
                    return new AuthResponse(newAccessToken, newRefreshToken, "Token refreshed successfully");
                }).orElse(null);
    }
}
