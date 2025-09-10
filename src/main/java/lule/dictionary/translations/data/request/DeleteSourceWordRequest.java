package lule.dictionary.translations.data.request;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.language.service.Language;

@Builder
public record DeleteSourceWordRequest(@NonNull String sourceWord,
                                      @NonNull String targetWord,
                                      @NonNull Language systemLanguage,
                                      @NonNull String owner,
                                      int selectedWordId,
                                      boolean isPhrase) implements TranslationsRequest {
}
