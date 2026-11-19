package com.solenia.auth_service.adapter.web.controller;

import com.solenia.auth_service.application.dto.AuthResponse;
import com.solenia.auth_service.application.dto.LoginRequest;
import com.solenia.auth_service.application.dto.RegisterRequest;
import com.solenia.auth_service.application.dto.UserResponse;
import com.solenia.auth_service.application.port.in.AuthService;
import com.solenia.auth_service.application.port.in.UserService;
import jakarta.validation.Valid;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService){

        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registrarUsuario (@Valid @RequestBody RegisterRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registrarUsuario(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal Long id){
        return ResponseEntity.of(userService.buscarUsuarioPorId(id));
    }
}
