package lule.dictionary.configuration.security.filter.osLanguage;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lule.dictionary.service.language.Language;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SystemLanguageFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String sourceLanguageHeader = request.getHeader("sourceLanguage");
        if(sourceLanguageHeader != null) {
            Language sourceLanguage = parseHeader(sourceLanguageHeader);
            updateLanguageContext(sourceLanguage);
            filterChain.doFilter(request, response);
        }
        updateLanguageContext(Language.EN);
        filterChain.doFilter(request, response);
    }

    private void updateLanguageContext(Language sourceLanguage) {
        SystemLanguageContext.set(sourceLanguage);
    }

    private Language parseHeader(String sourceLanguageHeader) {
        try {
            System.out.println("de source language: " + sourceLanguageHeader);
            String formattedHeader = sourceLanguageHeader.toUpperCase();
            return Language.valueOf(formattedHeader);
        } catch (IllegalArgumentException e) {
            return Language.EN;
        }
    }
}
