package lule.dictionary.entity.application.interfaces.userProfile;

import lule.dictionary.enumeration.Language;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails {
    Language sourceLanguage();
    Language targetLanguage();
}
