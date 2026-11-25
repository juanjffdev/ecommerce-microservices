package com.solenia.auth_service.application.usecase;

import com.solenia.auth_service.application.dto.AuthResponse;
import com.solenia.auth_service.application.dto.LoginRequest;
import com.solenia.auth_service.application.port.in.AuthService;
import com.solenia.auth_service.application.port.out.GeneratedToken;
import com.solenia.auth_service.application.port.out.PasswordHasher;
import com.solenia.auth_service.application.port.out.UserRepository;
import com.solenia.auth_service.domain.exception.InvalidCredentialException;
import com.solenia.auth_service.domain.model.User;
import com.solenia.auth_service.infrastructure.security.JwtService;
import com.solenia.commons.logging.AppLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.RequiredArgsConstructor;

@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordHasher passwordHasher;

    private final JwtService jwtService;

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialException::new);

        if (!passwordHasher.matches(request.password(),user.getPassword())){
            AppLogger.debug(log,"INVALID_USER_CREDENTIAL","La contraseña ingresada no es válida");
            throw new InvalidCredentialException();
        }

        GeneratedToken token = jwtService.generateToken(user);
        AppLogger.info(log,"USER_LOGGED","El usuario se ha logeado",user.getEmail());
        return new AuthResponse(token.value(),"Bearer",token.expiresin());
    }
}

