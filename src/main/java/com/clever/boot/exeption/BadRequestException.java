package com.clever.boot.exeption;

public class BadRequestException extends RuntimeException{
    public BadRequestException (String message) {
        super(message);
    }
}
