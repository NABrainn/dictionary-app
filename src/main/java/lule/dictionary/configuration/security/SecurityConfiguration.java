package lule.dictionary.configuration.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.configuration.security.filter.JwtAuthenticationFilter;
import lule.dictionary.configuration.security.filter.timezone.TimezoneFilter;
import lule.dictionary.cookie.service.CookieService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.csrf.*;
import org.springframework.security.web.savedrequest.NullRequestCache;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {

    private final CookieService cookieService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final TimezoneFilter timezoneFilter;

    @Value("${app.security.cookie-secure:true}")
    private boolean secure;

    @Value("${spring.security.jwt.expiration}")
    private long expiration;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        log.debug("Creating AuthenticationManager");
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository csrfTokenRepository = new CookieCsrfTokenRepository();
        csrfTokenRepository.setCookieCustomizer(cookie -> cookie
                .sameSite("Lax")
                .httpOnly(true)
                .secure(secure)
                .maxAge(expiration)
                .path("/"));
        csrfTokenRepository.setCookieName("XSRF-TOKEN");
        return csrfTokenRepository;
    }

    @Bean
    public XorCsrfTokenRequestAttributeHandler csrfHandler() {
        XorCsrfTokenRequestAttributeHandler requestHandler = new XorCsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName(null);
        return requestHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfTokenRepository())
                        .csrfTokenRequestHandler(csrfHandler()))
                .securityMatcher("/**")
                .authorizeHttpRequests(conf -> conf
                        .requestMatchers("/htmx.min.js", "/util.js", "/output.css", "/images/icon.png", "/favicon.ico", "/error/**", "/auth/**", "/localization/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .addLogoutHandler(new CookieClearingLogoutHandler("jwt", "X-XSRF"))
                        .logoutSuccessHandler((request, response, authentication) -> response.sendRedirect("/auth/login")))
                .requestCache(cache -> cache.requestCache(new NullRequestCache()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.warn("Authentication failed for request: uri={}, error={}",
                                    request.getRequestURI(), authException.getMessage(), authException);
                            response.sendRedirect("/auth/login?timeout=true");
                            CsrfToken csrfToken = csrfTokenRepository().generateToken(request);
                            csrfTokenRepository().saveToken(csrfToken, request, response);
                        }))
                .addFilterBefore(timezoneFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}