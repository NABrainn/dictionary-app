package lule.dictionary.configuration.security.filter.osLanguage;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lule.dictionary.language.service.Language;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SystemLanguageFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String systemLanguage = request.getHeader("uiLanguage");

        if (session.getAttribute("sourceLanguage") == null) {
            Language language = parseHeader(systemLanguage);
            session.setAttribute("sourceLanguage", language);
        }

        filterChain.doFilter(request, response);
    }

    private Language parseHeader(String systemLanguageHeader) {
        try {
            String formattedHeader = systemLanguageHeader != null ? systemLanguageHeader.toUpperCase() : "EN";
            return Language.valueOf(formattedHeader);
        } catch (IllegalArgumentException e) {
            return Language.EN;
        }
    }
}
