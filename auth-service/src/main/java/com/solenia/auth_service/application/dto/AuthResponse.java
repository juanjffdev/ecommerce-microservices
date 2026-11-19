package com.solenia.auth_service.application.dto;

public record AuthResponse(
        String accesToken,
        String tokenType,
        Long expiresin
) {}
