package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExpeptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorObject> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {

        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.NOT_FOUND.toString());
        errorObject.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorObject);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ErrorObject> handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException exception) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.UNAUTHORIZED.toString());
        errorObject.setMessage(exception.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorObject);
    }

    @ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<ErrorObject> handleAuthenticationException(AuthenticationException ex) {
        ErrorObject re = new ErrorObject(HttpStatus.UNAUTHORIZED.toString(),
                "Your session expired, please return to login page", new Date());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(re);
    }

    @ExceptionHandler({ HttpServerErrorException.class })
    public ResponseEntity<ErrorObject> handleHttpServerErrorException(HttpServerErrorException ex) {
        ErrorObject re = new ErrorObject(HttpStatus.SERVICE_UNAVAILABLE.toString(),
                "Failure getting api data. Please try again later.", new Date());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(re);
    }
    @ExceptionHandler({SavePredictionException.class})
    public ResponseEntity<ErrorObject> handleSavePredictionException(SavePredictionException ext) {
        ErrorObject re = new ErrorObject(HttpStatus.BAD_REQUEST.toString(),
            ext.getMessage(), new Date());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(re);
    }
}

