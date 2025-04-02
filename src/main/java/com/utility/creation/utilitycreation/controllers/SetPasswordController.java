package com.utility.creation.utilitycreation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utility.creation.utilitycreation.model.entity.SetPassword;
import com.utility.creation.utilitycreation.service.SetPasswordService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/password")
public class SetPasswordController {

    @Autowired
    private SetPasswordService setPasswordService;


    @PostMapping("/set")
    public ResponseEntity<String> setPassword(@Valid @RequestBody SetPassword request) {
        String response = setPasswordService.setPassword(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    
}
