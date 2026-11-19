package com.solenia.auth_service.infrastructure.config;

import com.solenia.auth_service.infrastructure.security.JwtAuthenticationFilter;
import com.solenia.auth_service.infrastructure.security.JwtService;
import com.solenia.auth_service.infrastructure.security.SecurityErrorWriter;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/* @EnableSecurity establece una seguridad básica para nuestra aplicación.
            La clase SecurityFilterChain permite personalizar el filtro de peticiones HTTP.

                           [ CLIENTE HTTP ]
                                  ▼
                   [ Otros filtros como Csrf, cors ]
                                  ▼
                   [ Nuestro filtro personalizado ]
                                  ▼
               [ UsernamePasswordAuthenticationFilter ] Filtro nativo de Spring
                                  ▼
                       [ Filtros posteriores ] FilterSecurityInterceptor - verifica roles/permisos
                                  ▼
                      [END POINT CONTROLADORES]                                             */
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtFilter,
            SecurityErrorWriter securityErrorWriter
    ) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)//1) Peticiones de diferentes dominios
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)

                //2) Establecemos la aplicación stateless, así spring no guarda registro de sesiones.
                .sessionManagement(sesssion -> sesssion
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                //3) Aca manejamos el acceso a nuestras URL
                .authorizeHttpRequests(authorize -> authorize
                        .dispatcherTypeMatchers(DispatcherType.ERROR)
                        .permitAll()

                        //Definimos el acceso a nuestras URL para POST
                        .requestMatchers(
                                HttpMethod.POST,
                                "/auth/register",
                                "/auth/login"
                        ).permitAll()          //A estas URL le permitimos el acceso cualquiera

                        //Definimos el acceso a nuestras URL para GET
                        .requestMatchers(
                                HttpMethod.GET,
                                "/auth/me"
                        ).hasAnyRole("USER","ADMIN")        //A estas URL le permitimos el acceso
                                                                  //con rol de user o admin
                        .requestMatchers(
                                HttpMethod.GET,
                                "/users",
                                "/users/id/**",
                                "/users/email/**"
                        ).hasAnyRole("ADMIN")

                //      .anyRequest().authenticated() //Cualquier otra ruta necesita autorización
                        .anyRequest().denyAll()       //Cualquier otra ruta queda denegada
                )

                //4) Agregamos nuestro filtro.
                .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class)



                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint((request, response, authException) ->
                               securityErrorWriter.write(
                                       response,
                                       403,
                                       "ACCESS_DENIED",
                                       "Debes presentar un roken válido"
                               )
                        )
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                        securityErrorWriter.write(
                                                response,
                                                403,
                                                "ACCESS_DENIED",
                                                "No tienes permisos para acceder a este recurso"
                                        )
                        )
                )

                .build();
    }
}
