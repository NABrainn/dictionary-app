package lule.dictionary.userProfiles.data;

import lombok.NonNull;
import lule.dictionary.language.service.Language;

public record ChangeTargetLanguageRequest(@NonNull Language language) implements ChangeLanguageRequest {
    public static ChangeTargetLanguageRequest of(Language language) {
        return new ChangeTargetLanguageRequest(language);
    }
}
