package lule.dictionary.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
public class JwtService {
    @Value("${spring.security.jwt.secret}")
    private String secret;

    @Value("${spring.security.jwt.expiration}")
    private long expiration;

    // Generate a SecretKey from the secret string
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Generate JWT token
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token, String username) {
        final String tokenUsername = getUsernameFromToken(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token)
                .getExpiration()
                .before(Date.from(Instant.now()));
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey()) // Use SecretKey for verification
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
