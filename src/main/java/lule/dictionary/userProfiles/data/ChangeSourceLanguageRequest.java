package lule.dictionary.userProfiles.data;

import lombok.NonNull;
import lule.dictionary.language.service.Language;

public record ChangeSourceLanguageRequest(@NonNull Language language) implements ChangeLanguageRequest {
    public static ChangeSourceLanguageRequest of(Language language) {
        return new ChangeSourceLanguageRequest(language);
    }
}
