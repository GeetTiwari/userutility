package com.utility.creation.utilitycreation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModulesDto {
    private int moduleId;
    private String module;
    private String description;

    private LocalDate createdDate;
    private LocalTime createdTime;
    private LocalDate modifiedDate;
    private LocalTime modifiedTime;
}

