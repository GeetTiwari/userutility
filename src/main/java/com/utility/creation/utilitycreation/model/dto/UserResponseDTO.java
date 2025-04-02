package com.utility.creation.utilitycreation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private String id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private boolean verified;
}
