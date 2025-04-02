package com.utility.creation.utilitycreation.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "Modules")
public class Modules extends CommonEntity{

    @Id
    @Indexed
    private int moduleId;
    @NotBlank(message = "Module name cannot be blank")
    private String module;
    private String description;

}

