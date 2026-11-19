package com.example.curso_api.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {

        super(message);
    }
}
