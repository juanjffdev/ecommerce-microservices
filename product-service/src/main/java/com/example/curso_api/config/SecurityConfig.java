package com.example.curso_api.config;

import com.example.curso_api.security.JwtAuthenticationFilter;
import com.example.curso_api.security.JwtService;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtService jwtService
    ) throws Exception {

        String[] catalogo = {
                "/api/productos",
                "/api/productos/**",
                "/api/categorias",
                "/api/categorias/**"
        };

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(authorize -> authorize
                        .dispatcherTypeMatchers(DispatcherType.ERROR)
                        .permitAll()

                        .requestMatchers(HttpMethod.GET, catalogo)
                        .permitAll()

                        .requestMatchers(HttpMethod.POST, catalogo)
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, catalogo)
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, catalogo)
                        .hasRole("ADMIN")

                        .anyRequest().denyAll()
                )

                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint((request, response, exception) -> {
                            response.setStatus(401);
                            response.setHeader("WWW-Authenticate", "Bearer");
                        })
                        .accessDeniedHandler((request, response, exception) ->
                                response.setStatus(403)
                        )
                )

                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtService),
                        UsernamePasswordAuthenticationFilter.class
                )

                .build();
    }
}