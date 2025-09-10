package lule.dictionary.translations.data.request;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.language.service.Language;

@Builder
public record UpdateTranslationFamiliarityRequest(@NonNull String targetWord,
                                                  @NonNull Familiarity familiarity,
                                                  @NonNull Language sourceLanguage,
                                                  @NonNull Language targetLanguage,
                                                  @NonNull Language systemLanguage,
                                                  @NonNull String owner,
                                                  int selectedWordId,
                                                  boolean isPhrase) {
}
