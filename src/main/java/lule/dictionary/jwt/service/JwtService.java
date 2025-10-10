package lule.dictionary.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class JwtService {
    @Value("${spring.security.jwt.secret}")
    private String secret;

    @Value("${spring.security.jwt.expiration}")
    private long expiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        log.info("The JWT secret is: {}", secret);
        if (secret == null || secret.trim().isEmpty()) {
            log.error("JWT secret is null or empty");
            throw new IllegalArgumentException("JWT secret key is missing or empty in configuration");
        }
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            log.error("JWT secret key length is {} bytes, but must be at least 32 bytes", keyBytes.length);
            throw new IllegalArgumentException("JWT secret key must be at least 32 bytes long");
        }
        secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        Instant now = Instant.now();
        Instant expirationTime = now.plus(expiration, ChronoUnit.MILLIS);

        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expirationTime))
                .signWith(secretKey)
                .compact();
    }

    public Optional<String> getUsernameFromToken(String token) {
        try {
            return Optional.ofNullable(Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject());
        } catch (Exception e) {
            log.warn("Failed to extract username from token: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public boolean validateToken(String token, String username) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject().equals(username) && !claims.getExpiration().toInstant().isBefore(Instant.now());
        } catch (Exception e) {
            log.warn("Invalid token: {}", e.getMessage());
            return false;
        }
    }
}
