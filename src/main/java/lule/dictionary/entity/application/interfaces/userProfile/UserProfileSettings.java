package lule.dictionary.entity.application.interfaces.userProfile;

import lule.dictionary.enumeration.Language;

public interface UserProfileSettings {
    Language sourceLanguage();
    Language targetLanguage();
}
