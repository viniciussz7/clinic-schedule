package com.clinic.exception;

public class SpecialtyAlreadyExistsException extends RuntimeException{
    public SpecialtyAlreadyExistsException(String message){
        super(message);
    }
}
