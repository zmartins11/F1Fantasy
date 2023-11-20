package com.example.demo.exception;

import ch.qos.logback.core.encoder.EchoEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class GlobalExpeptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorObject> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {

        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.BAD_REQUEST.toString());
        errorObject.setMessage(ex.getMessage());

        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ErrorObject> handleAuthenticationCredentialsNotFoundException(AuthenticationCredentialsNotFoundException exception) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.BAD_REQUEST.toString());
        errorObject.setMessage(exception.getMessage());

        return new ResponseEntity<ErrorObject>(errorObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ AuthenticationException.class })
    @ResponseBody
    public ResponseEntity<ErrorObject> handleAuthenticationException(AuthenticationException ex) {
        ErrorObject re = new ErrorObject(HttpStatus.UNAUTHORIZED.toString(),
                "Your session expired, please return to login page", new Date());
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(re);
    }

    @ExceptionHandler({ HttpServerErrorException.class })
    @ResponseBody
    public ResponseEntity<ErrorObject> handleHttpServerErrorException(Exception ex) {
        ErrorObject re = new ErrorObject(HttpStatus.SERVICE_UNAVAILABLE.toString(),
                "Failure getting api data. Please try again later.", new Date());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(re);
    }


}

