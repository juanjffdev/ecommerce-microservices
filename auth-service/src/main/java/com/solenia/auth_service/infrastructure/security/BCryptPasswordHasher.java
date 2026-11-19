package com.solenia.auth_service.infrastructure.security;

import com.solenia.auth_service.application.port.out.PasswordHasher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordHasher implements PasswordHasher {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String hash (String rawPassword){

        return encoder.encode(rawPassword);
    }

    @Override
    public boolean matches (String rawPassword, String hash){
        return encoder.matches(rawPassword, hash);
    }
}
