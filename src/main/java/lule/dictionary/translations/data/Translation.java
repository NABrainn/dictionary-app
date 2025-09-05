package lule.dictionary.translations.data;

import lule.dictionary.userProfiles.data.UserProfileSettings;

public interface Translation extends TranslationDetails, UserProfileSettings {
    String owner();
}
