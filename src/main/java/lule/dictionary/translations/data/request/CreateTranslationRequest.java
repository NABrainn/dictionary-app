package lule.dictionary.translations.data.request;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.language.service.Language;

@Builder
public record CreateTranslationRequest(int documentId,
                                       int selectedWordId,
                                       @NonNull String username,
                                       boolean isPhrase,
                                       @NonNull String targetWord,
                                       @NonNull Language sourceLanguage,
                                       @NonNull Language targetLanguage,
                                       @NonNull Language systemLanguage) {
}
