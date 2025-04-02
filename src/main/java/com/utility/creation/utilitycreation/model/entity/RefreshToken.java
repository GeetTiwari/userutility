package com.utility.creation.utilitycreation.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "RefreshToken")
public class RefreshToken {
    
    private String accessToken;
    @Indexed
    private String refreshToken;
    @Id
    @Indexed
    private String email;
}
