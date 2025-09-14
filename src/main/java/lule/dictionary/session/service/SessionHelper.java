package lule.dictionary.session.service;

import jakarta.servlet.http.HttpSession;
import lule.dictionary.language.service.Language;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class SessionHelper {

    public boolean getOrFalse(HttpSession session, String key) {
        if (session == null || key == null) {
            throw new IllegalArgumentException("Session or key cannot be null");
        }
        return session.getAttribute(key) instanceof Boolean && (boolean) session.getAttribute(key);
    }

    public <T> List<T> getList(HttpSession session, String key, Class<T> elementType) {
        if (session == null || key == null) {
            throw new IllegalArgumentException("Session or key cannot be null");
        }
        return Stream.of(session.getAttribute(key))
                .filter(Objects::nonNull)
                .filter(attribute -> attribute instanceof List<?>)
                .map(attribute -> (List<?>) attribute)
                .filter(list -> list.stream().allMatch(item -> item == null || elementType.isInstance(item)))
                .map(list -> {
                    @SuppressWarnings("unchecked")
                    List<T> result = (List<T>) list;
                    return (List<T>) result;
                })
                .findFirst()
                .orElseGet(List::of);
    }

    public Language getUILanguage(HttpSession httpSession) {
        return switch (httpSession.getAttribute("sourceLanguage")) {
            case null -> Language.EN;
            case Language language -> language;
            default -> throw new IllegalStateException("Unexpected value: " + httpSession.getAttribute("sourceLanguage"));
        };
    }
}
