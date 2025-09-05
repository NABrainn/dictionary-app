package lule.dictionary.userProfiles.data;

import lule.dictionary.language.service.Language;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails {
    Language sourceLanguage();
    Language targetLanguage();
    Language userInterfaceLanguage();
    int dailyStreak();
}
