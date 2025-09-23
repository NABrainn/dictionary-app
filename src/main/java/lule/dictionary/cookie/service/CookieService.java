package lule.dictionary.cookie.service;

import jakarta.servlet.http.Cookie;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    @Value("${spring.security.jwt.expiration}")
    private long expiration;

    public Cookie createJwtCookie(@NonNull String key, @NonNull String token) {
        if (token.trim().isEmpty()) {
            throw new IllegalArgumentException("JWT token cannot be null or empty");
        }
        Cookie tokenCookie = new Cookie(key, token);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");
        tokenCookie.setSecure(true);
        tokenCookie.setMaxAge((int) expiration);
        tokenCookie.setAttribute("SameSite", "Strict");
        return tokenCookie;
    }

    public Cookie deleteJwtCookie(@NonNull String key) {
        Cookie tokenCookie = new Cookie(key, null);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");
        tokenCookie.setSecure(true);
        tokenCookie.setMaxAge(0);
        tokenCookie.setAttribute("SameSite", "Strict");
        return tokenCookie;
    }
}