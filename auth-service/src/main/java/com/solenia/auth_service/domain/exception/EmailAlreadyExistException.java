package com.solenia.auth_service.domain.exception;

public class EmailAlreadyExistException extends RuntimeException {

    public EmailAlreadyExistException() {

        super("El correo ya está registrado kp");
    }
}
