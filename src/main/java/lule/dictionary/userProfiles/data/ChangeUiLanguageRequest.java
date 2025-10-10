package lule.dictionary.userProfiles.data;

import lombok.NonNull;
import lule.dictionary.language.service.Language;

public record ChangeUiLanguageRequest(@NonNull Language language) implements ChangeLanguageRequest {
    public static ChangeUiLanguageRequest of(Language language) {
        return new ChangeUiLanguageRequest(language);
    }
}
