package com.utility.creation.utilitycreation.model.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String message;
    private String accessToken;
    private String refreshToken;
}