package com.example.curso_api.config;

import com.example.curso_api.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public JwtService jwtService(
            @Value("${app.jwt.secret}") String secret
    ){
      return new JwtService(secret);
    }
}
