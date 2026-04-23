package com.clinic.exception;

public class CrmAlreadyExistsException extends RuntimeException {
    public CrmAlreadyExistsException(String message) {
        super(message);
    }
}
