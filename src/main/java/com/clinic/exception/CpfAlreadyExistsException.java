package com.clinic.exception;

public class CpfAlreadyExistsException extends RuntimeException{
    public CpfAlreadyExistsException(String message) {
        super(message);
    }
}
