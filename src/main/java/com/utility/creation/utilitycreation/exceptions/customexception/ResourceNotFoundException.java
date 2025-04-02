package com.utility.creation.utilitycreation.exceptions.customexception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }
}
