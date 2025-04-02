package com.utility.creation.utilitycreation.exceptions.globalhandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.utility.creation.utilitycreation.exceptions.customexception.EmailSendException;
import com.utility.creation.utilitycreation.exceptions.customexception.ExistingEmailException;
import com.utility.creation.utilitycreation.exceptions.customexception.ResourceNotFoundException;
import com.utility.creation.utilitycreation.exceptions.customexception.TokenExpiredException;
import com.utility.creation.utilitycreation.exceptions.customexception.UserAlreadyVerifiedException;
import com.utility.creation.utilitycreation.exceptions.customexception.UserNotFoundException;
import com.utility.creation.utilitycreation.model.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExistingEmailException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleExistingEmailException(Exception ex, WebRequest request) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), new Date(), ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(EmailSendException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleEmailSendException(Exception ex, WebRequest request) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), new Date(), ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(Exception ex, WebRequest request) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.toString(), new Date(), ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(Exception ex, WebRequest request) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.toString(), new Date(), ex.getMessage(), request.getDescription(false));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.toString(), new Date(), "Validation failed",
                errors.toString() + " | Path: " + request.getDescription(false));
    }

    @ExceptionHandler(UserAlreadyVerifiedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserAlreadyVerifiedException(Exception ex, WebRequest request) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), new Date(), ex.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleTokenExpiredException(Exception ex, WebRequest request) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.toString(), new Date(), ex.getMessage(),
                request.getDescription(false));
    }
}