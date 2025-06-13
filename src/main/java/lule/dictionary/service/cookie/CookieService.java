package lule.dictionary.service.cookie;

import jakarta.servlet.http.Cookie;
import lombok.NonNull;
import lule.dictionary.service.jwt.dto.TokenPair;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    public Cookie createJwtCookie(@NonNull String key, @NonNull TokenPair tokenPair) {
        Cookie tokenCookie = new Cookie(key, tokenPair.accessToken());
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");
        tokenCookie.setSecure(true);
        tokenCookie.setMaxAge(24 * 60 * 60);
        tokenCookie.setAttribute("SameSite", "Lax");
        return tokenCookie;
    }

    public Cookie deleteJwtCookie(@NonNull String key) {
        Cookie tokenCookie = new Cookie(key, null);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");
        tokenCookie.setSecure(true);
        tokenCookie.setMaxAge(0);
        tokenCookie.setAttribute("SameSite", "Lax");
        return tokenCookie;
    }
}
