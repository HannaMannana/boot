package com.clever.boot.exeption;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException{

    private final String message;
    private final HttpStatus httpStatus;

    public ApplicationException(String message, HttpStatus httpStatus) {
        super();
        this.message = message;
        this.httpStatus = httpStatus;
    }
}



