package com.app.controller;

import com.app.controller.dto.response.MessageResponse;
import com.app.exception.IncompleteFieldsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> throwGeneralException(Exception ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return ResponseEntity.internalServerError()
                .body(new MessageResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        errorMap)
                );
    }

    @ExceptionHandler(IncompleteFieldsException.class)
    public ResponseEntity<MessageResponse> handleIncompleteFieldsException(
            IncompleteFieldsException ex
    ) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(new MessageResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        errorMap)
                );
    }

}
