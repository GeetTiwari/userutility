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

import com.utility.creation.utilitycreation.model.dto.ModulesDto;
import com.utility.creation.utilitycreation.service.ModuleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/module")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @PostMapping
    public ModulesDto addModule(@Valid @RequestBody ModulesDto moduleDto) {
        return moduleService.addModule(moduleDto);
    }

    @GetMapping
    public List<ModulesDto> getAllModules() {
        return moduleService.getAllModules();
    }

    @GetMapping("/{id}")
    public ModulesDto getModuleById(@PathVariable int id) {
        return moduleService.getModuleById(id);
    }

    @PutMapping("/{id}")
    public ModulesDto updateModuleById(@PathVariable int id, @RequestBody ModulesDto newModuleDto) {
        return moduleService.updateModuleById(id, newModuleDto);
    }

    @DeleteMapping("/{id}")
    public String deleteModuleById(@PathVariable int id) {
        moduleService.deleteModuleById(id);
        return "module deleted ";

    }

}
