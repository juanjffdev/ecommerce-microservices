package com.solenia.auth_service.application.dto;

public record UserResponse(
        Long id,
        String name,
        String email,
        String role
) {
}
