package com.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public abstract class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message){
        super(message);
    }
}
