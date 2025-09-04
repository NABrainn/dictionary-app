package lule.dictionary.language.service;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record LanguageData(@NonNull Language language,
                           @NonNull String fullName,
                           @NonNull String languageCode,
                           @NonNull String imgPath) {
}
