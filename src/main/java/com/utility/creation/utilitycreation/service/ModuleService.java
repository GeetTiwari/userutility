package com.utility.creation.utilitycreation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.utility.creation.utilitycreation.model.dto.ModulesDto;
import com.utility.creation.utilitycreation.model.entity.Modules;
import com.utility.creation.utilitycreation.repository.ModuleRepository;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    public ModulesDto addModule(ModulesDto moduleDto) {
        if (moduleRepository.findById(String.valueOf(moduleDto.getModuleId())).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Module ID must be unique");
        }

        Modules module = new Modules(moduleDto.getModuleId(), moduleDto.getModule(), moduleDto.getDescription());
        Modules savedModule = moduleRepository.save(module);
        return new ModulesDto(savedModule.getModuleId(), savedModule.getModule(), savedModule.getDescription(),savedModule.getCreatedDate(),savedModule.getCreatedTime(),savedModule.getModifiedDate(),savedModule.getModifiedTime());
    }

    // run a fetch query in the Redis Database
    // to get a list of all the Modules
    public List<ModulesDto> getAllModules() {
        List<ModulesDto> allModuleDtos = new ArrayList<>();
        moduleRepository.findAll().forEach(module -> 
            allModuleDtos.add(new ModulesDto(module.getModuleId(), module.getModule(), module.getDescription(),module.getCreatedDate(),module.getCreatedTime(),module.getModifiedDate(),module.getModifiedTime()))
        );
        return allModuleDtos;
    }

    // fetch operation to get Module using an ID
    public ModulesDto getModuleById(int id) {
        Optional<Modules> optionalModule = moduleRepository.findById(String.valueOf(id));
        if (optionalModule.isPresent()) {
            Modules module = optionalModule.get();
            return new ModulesDto(module.getModuleId(), module.getModule(), module.getDescription(),module.getCreatedDate(),module.getCreatedTime(),module.getModifiedDate(),module.getModifiedTime());
        }
        return null;
    }

    // update operation to existing Module using an ID
    public ModulesDto updateModuleById(int id, ModulesDto newModuleDto) {
        Optional<Modules> existingModule = moduleRepository.findById(String.valueOf(id));
    
        if (existingModule.isPresent()) {
            Modules updatedModule = existingModule.get();
    
            // Keep the moduleId unchanged (do not set it again)
            updatedModule.setModule(newModuleDto.getModule());
            updatedModule.setDescription(newModuleDto.getDescription());
    
            Modules savedModule = moduleRepository.save(updatedModule);
            return new ModulesDto(savedModule.getModuleId(), savedModule.getModule(), savedModule.getDescription(),savedModule.getCreatedDate(),savedModule.getCreatedTime(),savedModule.getModifiedDate(),savedModule.getModifiedTime());
        }
    
        return null;
    } 

    // delete the existing Module
    public void deleteModuleById(int id) {
        moduleRepository.deleteById(String.valueOf(id));
    }

}
