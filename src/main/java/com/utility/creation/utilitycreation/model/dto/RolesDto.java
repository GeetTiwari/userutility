package com.utility.creation.utilitycreation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolesDto {
    private int roleId;

    private String roleName;

    private LocalDate createdDate;
    private LocalTime createdTime;
    private LocalDate modifiedDate;
    private LocalTime modifiedTime;
}
