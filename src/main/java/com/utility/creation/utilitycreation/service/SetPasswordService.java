package com.utility.creation.utilitycreation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.utility.creation.utilitycreation.model.entity.SetPassword;
import com.utility.creation.utilitycreation.model.entity.User;
import com.utility.creation.utilitycreation.repository.UserRepo;

import jakarta.validation.Valid;

@Service
public class SetPasswordService {

    @Autowired
    private UserRepo userRepo;

    public String setPassword(@Valid @RequestBody SetPassword request) {

        // Check if user exists
        User existingUser = userRepo.findByEmail(request.getEmail());

        if (existingUser != null) {
            return "Password set successfully for email: " + request.getEmail();
        }
        return "User with this email does not exists.";
    }

}
