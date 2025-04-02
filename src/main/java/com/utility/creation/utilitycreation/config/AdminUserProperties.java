package com.utility.creation.utilitycreation.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "admin")
public class AdminUserProperties {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String password;
    private String role;
}
