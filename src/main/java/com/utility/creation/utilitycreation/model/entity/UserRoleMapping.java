package com.utility.creation.utilitycreation.model.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "UserRoleMapping")
public class UserRoleMapping implements Serializable {

    @Id
    @Indexed
    private String mid;
    @Indexed
    private String id;
    @Indexed
    private int roleId;

    public UserRoleMapping(String userId, int roleId) {
        this.mid = "UserRoleMapping:mid:" + userId + ":" + roleId;  // Unique key per user-role
        this.id = userId;
        this.roleId = roleId;
    }
}