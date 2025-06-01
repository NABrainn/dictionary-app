package lule.dictionary.dto.application.implementation.translation;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.application.interfaces.translation.Translation;
import lule.dictionary.dto.application.interfaces.translation.TranslationDetails;
import lule.dictionary.dto.application.interfaces.userProfile.UserProfileSettings;

@Slf4j
public record DictionaryTranslation(
        @NonNull
        TranslationDetails translationDetails,
        @NonNull
        UserProfileSettings userProfileSettings,
        @NonNull
        String owner) implements Translation {
}
