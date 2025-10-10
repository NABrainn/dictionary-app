package lule.dictionary.controllerAdvice.data.navbar;

import lombok.NonNull;
import lule.dictionary.language.service.Language;

public record LanguageOption(@NonNull Language language,
                             @NonNull String imgUrl) {
    public static LanguageOption of(Language language, String imgUrl) {
        return new LanguageOption(language, imgUrl);
    }
}
