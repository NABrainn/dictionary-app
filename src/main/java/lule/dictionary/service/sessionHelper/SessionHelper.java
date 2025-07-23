package lule.dictionary.service.sessionHelper;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class SessionHelper {

    public <T> T getOrDefault(HttpSession session, String key, T defaultValue) {
        T value = (T) session.getAttribute(key);
        return value != null ? value : defaultValue;
    }}
