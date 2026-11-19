package com.solenia.auth_service.application.port.in;

import java.time.Instant;

public record UserClaim(
        long userId,
        String email,
        String role,
        Instant issuedAt,
        Instant expiration
) {
}
