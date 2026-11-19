package com.solenia.auth_service.application.port.out;

public interface PasswordHasher {
    String hash(String rawPassword);
    boolean matches (String rawPassword, String hash);
}
