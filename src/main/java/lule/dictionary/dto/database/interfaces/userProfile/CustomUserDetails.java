package lule.dictionary.dto.database.interfaces.userProfile;

import lule.dictionary.service.language.Language;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails {
    Language sourceLanguage();
    Language targetLanguage();
    int dailyStreak();
}
