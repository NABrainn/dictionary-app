package lule.dictionary.dto.database.interfaces.userProfile;

import lule.dictionary.service.language.Language;

public interface UserProfileSettings {
    Language sourceLanguage();
    Language targetLanguage();
}
