package com.utility.creation.utilitycreation.model.dto;

import com.utility.creation.utilitycreation.model.entity.UserRoleMapping;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data 
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    
    private String id;
    
    @NotBlank
    @Size(min=3, message= "First name should be of minimum 3 characters")
    private String firstName;

    @NotBlank
    @Size(min=3, message= "Last name should be of minimum 3 characters")
    private String lastName;

    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp = "^\\+?\\d{10,15}$", 
        message = "Invalid phone number. It must contain 10-15 digits and may start with a '+'"
    )
    private String phone;
    
    @NotBlank
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$",
            message = "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one numeric digit, and one special character (!@#$%^&*)"
    )
    private String password;

    private LocalDate createdDate;
    private LocalTime createdTime;
    private LocalDate modifiedDate;
    private LocalTime modifiedTime;
}
