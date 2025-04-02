package com.utility.creation.utilitycreation.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utility.creation.utilitycreation.model.dto.RolesDto;
import com.utility.creation.utilitycreation.model.entity.Roles;
import com.utility.creation.utilitycreation.service.RoleService;



@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping
    public RolesDto addRoles(@RequestBody RolesDto rolesDto) {
        return roleService.addRoles(rolesDto);
    }

    @GetMapping
    public List<RolesDto> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/{id}")
    public RolesDto getRolesById(@PathVariable int id) {
        return roleService.getRolesById(id);
    }

    @PutMapping("/{id}")
    public RolesDto updateRolesById(@PathVariable int id, @RequestBody RolesDto newRolesDto) {
        return roleService.updateRolesById(id, newRolesDto);
    }

    @DeleteMapping("/{id}")
    public String deleteRolesById(@PathVariable int id) {
        roleService.deleteRolesById(id);
        return "Role deleted successfully";
    }
}
