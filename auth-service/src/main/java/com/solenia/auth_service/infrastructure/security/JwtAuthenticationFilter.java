package com.solenia.auth_service.infrastructure.security;

import com.solenia.auth_service.application.port.in.UserClaim;
import com.solenia.auth_service.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.el.parser.Token;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.LongAccumulator;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final SecurityErrorWriter errorWriter;

    /*Ya lo genera @RequiredArgsConstructor para atributos final y @NotNull
    public JwtAuthenticationFilter (JwtTokenValidator jwtTokenValidator){
        this.jwtTokenValidator = jwtTokenValidator;
    }*/

    @Override//Este es nuestro filtro personalizado se aplica antes de llegar al filtro nativo de spring
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        //1) Ya está authenticado?
        if (SecurityContextHolder.getContext().getAuthentication() != null){
            filterChain.doFilter(request, response);
            return;
        }

        //2) Extraemos el token luego de verificar no null y que comience con Bearer
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            //doFilter le permite avanzar al siguiente filtro.
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7).trim();

        if (token.isEmpty()){
            unauthorized(response);
            return;
        }

        //3) Validamos la estructura, firma del token
        UserClaim userClaim;
        try {
            userClaim = jwtService.validateAndGetUserId(token);
        }catch (JwtException | IllegalArgumentException exception){
            SecurityContextHolder.clearContext();
            unauthorized(response);
            return;
        }

        //4) Validamos la expiracion del token.
        boolean isTokenExpired = jwtService.isExpired(userClaim.expiration());

        if (isTokenExpired){
            filterChain.doFilter(request,response);
            return;
        }

        //5) Autenticamos. Si llegamos hasta acá la firma es válida y aun no expiro
        var authority = new SimpleGrantedAuthority("ROLE_"+userClaim.role());

        var authentication = UsernamePasswordAuthenticationToken.authenticated(
                userClaim.userId(),
                null,
                List.of(authority)
        );

        // 6) Guardamos el contexto
        var context = SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        filterChain.doFilter(request,response);

    }

    public void unauthorized(HttpServletResponse response) throws IOException{
        errorWriter.write(
                response,
                401,
                "INVALID_TOKEN",
                "El token es inválido o ha expirado"
        );
    }
}
