package lule.dictionary.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.auth.service.SecurityContextService;
import lule.dictionary.jwt.service.JwtService;
import lule.dictionary.userProfiles.service.UserProfileService;
import lule.dictionary.userProfiles.service.exception.UserNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
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
    private final SecurityContextService securityContextService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        Optional<String> optionalJwt = (switch (request.getCookies()) {
            case Cookie[] ignored -> Arrays.stream(request.getCookies())
                    .filter(cookie -> "jwt".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst();
            case null -> Optional.empty();
        });

        if (optionalJwt.isEmpty()) {
            log.debug("No JWT cookie found, proceeding with filter chain");
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = optionalJwt.get();
        Optional<String> optionalUsername = jwtService.getUsernameFromToken(jwt);
        if (optionalUsername.isPresent()) {
            String username = optionalUsername.get();
            if(!securityContextService.isAuthenticated()) {
                try {
                    UserDetails userDetails = userProfileService.loadUserByUsername(username);
                    if (jwtService.validateToken(jwt, username)) {
                        WebAuthenticationDetails requestToken = new WebAuthenticationDetailsSource().buildDetails(request);
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        securityContextService.setJwtContext(authToken, requestToken);
                        log.debug("Authenticated user: {}", username);
                    }
                } catch (UserNotFoundException e) {
                    log.warn("User not found for username: {}", username);
                    filterChain.doFilter(request, response);
                    return;
                } catch (Exception e) {
                    log.error("Error during JWT authentication: {}", e.getMessage());
                    filterChain.doFilter(request, response);
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}