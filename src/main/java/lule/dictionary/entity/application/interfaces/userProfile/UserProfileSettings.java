package lule.dictionary.entity.application.interfaces.userProfile;

import lule.dictionary.service.language.Language;

public interface UserProfileSettings {
    Language sourceLanguage();
    Language targetLanguage();
}
