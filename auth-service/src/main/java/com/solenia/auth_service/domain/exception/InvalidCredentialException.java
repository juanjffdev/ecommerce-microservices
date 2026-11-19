package com.solenia.auth_service.domain.exception;

public class InvalidCredentialException extends RuntimeException {
    public InvalidCredentialException() {

        super("Credenciales incorrectas");
    }
}
