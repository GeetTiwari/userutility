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
@RedisHash(value = "User")
public class User extends CommonEntity{
    @Id
    @Indexed
    private String id; // "indexed" for faster retrieval,
                    
    private String firstName;

    private String lastName;

    private String phone;

    @Indexed
    private String email;

    private String password;

    private boolean verified;
   }
