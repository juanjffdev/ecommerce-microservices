package com.solenia.auth_service.application.port.in;

import com.solenia.auth_service.application.dto.AuthResponse;
import com.solenia.auth_service.application.dto.LoginRequest;

public interface AuthService {
    AuthResponse login(LoginRequest request);
}
