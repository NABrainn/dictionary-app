package lule.dictionary.dto.application.interfaces.userProfile;

import lule.dictionary.enumeration.Language;

public interface UserProfileSettings {
    Language sourceLanguage();
    Language targetLanguage();
}
