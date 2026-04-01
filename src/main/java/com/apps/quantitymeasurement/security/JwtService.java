package com.apps.quantitymeasurement.security;

import com.apps.quantitymeasurement.config.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final SecurityProperties securityProperties;
    private SecretKey signingKey;

    @PostConstruct
    void init() {
        byte[] keyBytes = securityProperties.getJwt().getSecret().getBytes(StandardCharsets.UTF_8);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication) {
        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return generateToken(authentication.getName(), roles, Map.of("authProvider", "LOCAL"));
    }

    public String generateToken(String subject, Collection<String> roles, Map<String, Object> extraClaims) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusMillis(securityProperties.getJwt().getExpirationMs());

        return Jwts.builder()
                .claims(extraClaims)
                .subject(subject)
                .issuer(securityProperties.getJwt().getIssuer())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .claim("roles", roles)
                .signWith(signingKey)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractClaims(token);
        Object rawRoles = claims.get("roles");

        if (rawRoles instanceof List<?> roleList) {
            return roleList.stream().map(String::valueOf).toList();
        }

        return List.of();
    }

    public Instant extractExpiration(String token) {
        return extractClaims(token).getExpiration().toInstant();
    }

    public boolean isTokenValid(String token) {
        Claims claims = extractClaims(token);
        return claims.getExpiration().toInstant().isAfter(Instant.now());
    }
}
