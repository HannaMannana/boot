package com.clever.boot.exeption;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ApplicationException.class})
    public ResponseEntity<Object> handleAppExceptions(
            ApplicationException ex, WebRequest request) {

        ApplicationExceptionResponseBody body = ApplicationExceptionResponseBody
                .builder()
                .message(ex.getMessage())
                .status(ex.getHttpStatus())
                .build();

        return handleExceptionInternal(ex, body, new HttpHeaders(), ex.getHttpStatus(), request);
    }
}