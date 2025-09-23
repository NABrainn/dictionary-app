package lule.dictionary.configuration.security;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.configuration.security.filter.JwtAuthenticationFilter;
import lule.dictionary.configuration.security.filter.timezone.TimezoneFilter;
import lule.dictionary.cookie.service.CookieService;
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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfiguration {

    private final CookieService cookieService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final TimezoneFilter timezoneFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        log.debug("Creating AuthenticationManager");
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring SecurityFilterChain");
        return http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/auth/logout"))
                .securityMatcher("/**")
                .authorizeHttpRequests(conf -> conf
                        .requestMatchers("/htmx.min.js", "/util.js", "/output.css", "/images/icon.png", "/favicon.ico", "/error/**", "/auth/**", "/localization/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout=true")
                        .addLogoutHandler((request, response, authentication) -> {
                            String username = authentication != null ? authentication.getName() : "anonymous";
                            log.info("Logging out user: {}", username);
                            log.debug("Logout request details: method={}, uri={}",
                                    request.getMethod(), request.getRequestURI());
                            Cookie cookie = cookieService.deleteJwtCookie("jwt");
                            response.addCookie(cookie);
                            log.info("JWT cookie cleared for user: {}", username);
                        })
                        .permitAll())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.warn("Authentication failed for request: uri={}, error={}",
                                    request.getRequestURI(), authException.getMessage(), authException);
                            response.sendRedirect("/auth/login?timeout=true");
                        }))
                .addFilterBefore(timezoneFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}