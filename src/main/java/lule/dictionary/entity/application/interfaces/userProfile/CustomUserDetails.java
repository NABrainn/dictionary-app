package lule.dictionary.entity.application.interfaces.userProfile;

import lule.dictionary.service.language.Language;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails {
    String offset();
    Language sourceLanguage();
    Language targetLanguage();
}
