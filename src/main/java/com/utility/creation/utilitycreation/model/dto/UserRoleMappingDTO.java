package com.utility.creation.utilitycreation.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleMappingDTO {
    private String mid;
    private String id;
    private List<Integer> roleIds;
    private List<String> roleNames;
}
