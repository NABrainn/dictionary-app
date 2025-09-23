package lule.dictionary.cookie.service;

import jakarta.servlet.http.Cookie;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CookieService {

    @Value("${spring.security.jwt.expiration}")
    private long expiration;

    @Value("${app.security.cookie-secure:true}")
    private boolean secure;

    public Cookie createJwtCookie(@NonNull String key, @NonNull String token) {
        if (token.trim().isEmpty()) {
            log.error("Attempted to create JWT cookie with empty token for key: {}", key);
            throw new IllegalArgumentException("JWT token cannot be null or empty");
        }
        Cookie tokenCookie = new Cookie(key, token);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");
        tokenCookie.setSecure(secure); // Consider making this configurable as per previous advice
        tokenCookie.setMaxAge((int) expiration);
        tokenCookie.setAttribute("SameSite", "Strict");
        log.info("Created JWT cookie: key={}, secure={}, maxAge={}, sameSite={}",
                key, tokenCookie.getSecure(), tokenCookie.getMaxAge(), tokenCookie.getAttribute("SameSite"));
        return tokenCookie;
    }

    public Cookie deleteJwtCookie(@NonNull String key) {
        Cookie tokenCookie = new Cookie(key, null);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");
        tokenCookie.setSecure(secure);
        tokenCookie.setMaxAge(0);
        tokenCookie.setAttribute("SameSite", "Strict");
        log.info("Deleted JWT cookie: key={}, secure={}, maxAge={}",
                key, tokenCookie.getSecure(), tokenCookie.getMaxAge());
        return tokenCookie;
    }
}