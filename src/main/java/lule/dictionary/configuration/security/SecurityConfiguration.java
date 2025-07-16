package lule.dictionary.configuration.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.configuration.security.filter.JwtAuthenticationFilter;
import lule.dictionary.configuration.security.filter.timezone.TimezoneFilter;
import lule.dictionary.dto.application.result.ServiceResult;
import lule.dictionary.dto.application.result.ServiceResultImp;
import lule.dictionary.service.cookie.CookieService;
import lule.dictionary.service.userProfile.UserProfileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.*;

import java.util.Map;


@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {

    private final UserProfileService userProfileService;
    private final CookieService cookieService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final TimezoneFilter timezoneFilter;
    private final BCryptPasswordEncoder encoder;

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userProfileService);
        daoAuthenticationProvider.setPasswordEncoder(encoder);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(customizer -> customizer
                        .csrfTokenRepository(csrfTokenRepository())
                )
                .authorizeHttpRequests(conf -> conf
                        .requestMatchers(
                                "/htmx.min.js",
                                "/output.css",
                                "/images/icon.png",
                                "/favicon.ico",
                                "/error/**",
                                "/auth/**"
                        ).permitAll()
                        .anyRequest().authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandler -> exceptionHandler
                        .authenticationEntryPoint((request, response, authException) -> {
                            String path = request.getRequestURI();
                            log.warn("Unauthenticated access to {}", path);
                            if (!"/auth/login".equals(path)) {
                                response.sendRedirect("/auth/login");
                            } else {
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                            }
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            switch (accessDeniedException) {
                                case MissingCsrfTokenException ignored -> response.sendError(HttpServletResponse.SC_FORBIDDEN, "Missing CSRF token");
                                case InvalidCsrfTokenException ignored -> response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
                                default -> response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
                            }
                        })
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .addLogoutHandler((request, response, authentication) -> {
                            log.info("Logging out user: {}", authentication != null ? authentication.getName() : "anonymous");
                            logout(response);
                        })
                        .permitAll()
                )
                .addFilterBefore(timezoneFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(daoAuthenticationProvider())
                .build();
    }

    public void logout(@NonNull HttpServletResponse httpServletResponse) {
        clearAuthentication();
        deleteJwtCookie(httpServletResponse);
    }

    private void clearAuthentication() {
        setAuthentication(null);
        clearContext();
    }

    private void clearContext() {
        SecurityContextHolder.clearContext();
    }

    private void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void deleteJwtCookie(HttpServletResponse response) {
        Cookie cookie = cookieService.deleteJwtCookie("jwt");
        response.addCookie(cookie);
    }
}
