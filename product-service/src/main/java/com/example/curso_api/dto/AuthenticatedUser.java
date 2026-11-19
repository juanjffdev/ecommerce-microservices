package com.example.curso_api.dto;

public record AuthenticatedUser(
        Long userId,
        String role
) {
}
