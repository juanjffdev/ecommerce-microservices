package com.example.curso_api.security;

import com.example.curso_api.dto.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class JwtService {

    private final JwtParser parser;

    public JwtService (String secret){

        var key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        this.parser = Jwts.parser()
                .verifyWith(key)
                .sig().clear().add(Jwts.SIG.HS256).and()
                .build();
    }

    public AuthenticatedUser validate(String token){

        Claims claims = parser.parseSignedClaims(token).getPayload();

        String subject = claims.getSubject();

        String role = claims.get("role",String.class);

        if (subject==null || subject.isBlank()
                || claims.getIssuedAt()==null
                ||claims.getExpiration()==null
                ||(!"USER".equals(role)&&!"ADMIN".equals(role))
        ){
            throw new JwtException("Datos del token inválidos");
        }

        long userId;

        try {
            userId = Long.parseLong(subject);
        }catch (NumberFormatException exception){
            throw new JwtException("Identificador inválido", exception);
        }

        return new AuthenticatedUser(userId,role);
    }
}
