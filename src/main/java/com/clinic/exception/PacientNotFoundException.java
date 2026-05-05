package com.clinic.exception;

public class PacientNotFoundException extends RuntimeException{
    public PacientNotFoundException(String message) {
        super(message);
    }
}
