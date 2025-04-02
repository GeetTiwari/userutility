package com.utility.creation.utilitycreation.exceptions.customexception;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
