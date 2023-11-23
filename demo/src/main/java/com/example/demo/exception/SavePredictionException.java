package com.example.demo.exception;

public class SavePredictionException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public SavePredictionException(String message) {
        super(message);
    }

}
