package com.utility.creation.utilitycreation.exceptions.customexception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
