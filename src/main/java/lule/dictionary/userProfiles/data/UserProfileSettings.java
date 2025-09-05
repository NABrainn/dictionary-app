package lule.dictionary.userProfiles.data;

import lule.dictionary.language.service.Language;

public interface UserProfileSettings {
    Language sourceLanguage();
    Language targetLanguage();
}
