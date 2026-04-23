package com.clinic.exception;

public class SpecialtyNotFoundException extends RuntimeException{
    public SpecialtyNotFoundException(String message){
        super(message);
    }
}
