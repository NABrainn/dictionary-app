package lule.dictionary.dto.application;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.service.language.Language;

@Builder
public record LanguageData(@NonNull Language language,
                           @NonNull String fullName,
                           @NonNull String languageCode,
                           @NonNull String imgPath) {
}
