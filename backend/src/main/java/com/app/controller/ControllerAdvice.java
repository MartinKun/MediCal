package com.app.controller;

import com.app.controller.dto.response.MessageResponse;
import com.app.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String errorMessage = error.getDefaultMessage();
            if (error instanceof FieldError) {
                String fieldName = ((FieldError) error).getField();
                errors.put(fieldName, errorMessage);
            } else {
                errors.put("error", errorMessage);
            }
        });
        return ResponseEntity.badRequest()
                .body(new MessageResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        errors)
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> throwGeneralException(
            Exception ex
    ) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return ResponseEntity.internalServerError()
                .body(new MessageResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        errorMap)
                );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<MessageResponse> throwBadRequestException(
            Exception ex
    ) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        errorMap)
                );
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<MessageResponse> throwConflictException(
            Exception ex
    ) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MessageResponse(
                        HttpStatus.CONFLICT.value(),
                        errorMap)
                );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<MessageResponse> throwNotFoundException(
            Exception ex
    ) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponse(
                        HttpStatus.NOT_FOUND.value(),
                        errorMap)
                );
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<MessageResponse> throwForbiddenException(
            Exception ex
    ) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new MessageResponse(
                        HttpStatus.FORBIDDEN.value(),
                        errorMap)
                );
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<MessageResponse> throwAuthenticationException(
            AuthenticationException ex
    ) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new MessageResponse(
                        HttpStatus.UNAUTHORIZED.value(),
                        errorMap)
                );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<MessageResponse> throwUnauthorizedException(
            Exception ex
    ) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new MessageResponse(
                        HttpStatus.UNAUTHORIZED.value(),
                        errorMap)
                );
    }

}
