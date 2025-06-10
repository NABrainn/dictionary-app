package lule.dictionary.factory;

import jakarta.servlet.http.Cookie;
import lombok.NonNull;
import lule.dictionary.service.jwt.dto.TokenPair;

public class CookieFactory {

    public static Cookie createJwtCookie(@NonNull String key, @NonNull TokenPair tokenPair) {
        Cookie tokenCookie = new Cookie(key, tokenPair.accessToken());
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");
        tokenCookie.setSecure(true);
        tokenCookie.setMaxAge(24 * 60 * 60);
        tokenCookie.setAttribute("SameSite", "Strict");
        return tokenCookie;
    }
}
