package com.solenia.auth_service.infrastructure.config;

import com.solenia.auth_service.application.dto.LoginRequest;
import com.solenia.auth_service.application.mapper.UserMapper;
import com.solenia.auth_service.application.port.in.AuthService;
import com.solenia.auth_service.application.port.in.UserService;
import com.solenia.auth_service.application.port.out.PasswordHasher;
import com.solenia.auth_service.application.port.out.UserRepository;
import com.solenia.auth_service.application.usecase.AuthServiceImpl;
import com.solenia.auth_service.application.usecase.UserServiceImpl;
import com.solenia.auth_service.infrastructure.security.BCryptPasswordHasher;
import com.solenia.auth_service.infrastructure.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanConfiguration {

    @Bean
    public UserMapper userMapper(){
        return new UserMapper();
    }
    //Spring encuentra JdbcUserRepository gracias a su @Repository
    @Bean //registramos UserServiceImp para inyectarlo
    public UserService userService(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordHasher passwordHasher){
        return new UserServiceImpl(
                userRepository,
                userMapper,
                passwordHasher);
    }

    @Bean
    public AuthService authService(
            UserRepository userRepository,
            PasswordHasher passwordHasher,
            JwtService jwtService
    ){
        return new AuthServiceImpl(
                userRepository,
                passwordHasher,
                jwtService
        );
    }

    @Bean
    public PasswordHasher passwordHasher(){
        return new BCryptPasswordHasher();
    }

    @Bean
    public JwtService jwtService(
            @Value ("${app.jwt.secret}") String secret,
            @Value ("${app.jwt.expiration-seconds}") long expirationSeconds
    ){
        return new JwtService(secret,expirationSeconds);
    }

}
