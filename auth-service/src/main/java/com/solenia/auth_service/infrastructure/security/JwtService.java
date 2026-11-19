package com.solenia.auth_service.infrastructure.security;

import com.solenia.auth_service.application.port.in.UserClaim;
import com.solenia.auth_service.application.port.out.GeneratedToken;
import com.solenia.auth_service.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.el.parser.Token;
import org.springframework.security.authentication.BadCredentialsException;

import javax.crypto.SecretKey;
import java.net.PortUnreachableException;
import java.time.Instant;
import java.util.Date;

public class JwtService {

    private final SecretKey key;

    private final Long expirationSeconds;

    private final Long REFRESH_WINDOW = 60*60*24L;

    public JwtService (String secret, Long expirationSeconds){
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expirationSeconds=expirationSeconds;
    }

    public GeneratedToken generateToken(User user) {
        Instant now = Instant.now();
        String token = Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("email",user.getEmail())
                .claim("role",user.getRole())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expirationSeconds)))
                .signWith(key,Jwts.SIG.HS256)
                .compact();

        return new GeneratedToken(token,expirationSeconds);
    }

    public UserClaim validateAndGetUserId (String token){
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .sig().clear().add(Jwts.SIG.HS256).and()
                .build()
                .parseSignedClaims(token)
                .getPayload();

        long Id = Long.parseLong(claims.getSubject());
        String subjetc = claims.getSubject();
        String email = claims.get("email", String.class);
        String role = claims.get("role",String.class);

        if (Id <= 0
                || subjetc.isEmpty()
                || email.isEmpty()
                || (!"USER".equals(role) && !"ADMIN".equals(role))
        ){
            throw new JwtException("Datos del token inválidos");
        }

        return new UserClaim(Id,
                email,
                role,
                claims.getIssuedAt().toInstant(),
                claims.getExpiration().toInstant()
        );
    }

    public boolean isExpired(Instant expiration){
        return !Instant.now().isBefore(expiration);
    }
}
