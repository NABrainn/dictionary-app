package lule.dictionary.configuration.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.jwt.service.JwtService;
import lule.dictionary.userProfiles.service.UserProfileService;
import lule.dictionary.userProfiles.service.exception.UserNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserProfileService userProfileService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final Optional<String> optionalJwt = getJwtFromCookie("jwt", request);

        if (optionalJwt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        if (!jwtService.isValidToken(optionalJwt.get())) {
            filterChain.doFilter(request, response);
            return;
        }

        final Optional<String> optionalUsername = jwtService.extractUsername(optionalJwt.get());

        if (optionalUsername.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userProfileService.loadUserByUsername(optionalUsername.get());

                if (jwtService.validateTokenForUser(optionalJwt.get(), userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (UserNotFoundException e) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> getJwtFromCookie(String jwtCookieName, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(cookie -> jwtCookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}