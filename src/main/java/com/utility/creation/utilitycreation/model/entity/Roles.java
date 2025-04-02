package com.utility.creation.utilitycreation.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "Roles")
public class Roles extends CommonEntity{

    @Id
    @Indexed
    private int roleId;
    @Indexed
    private String roleName;

}
