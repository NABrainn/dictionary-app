package lule.dictionary.translations.data.request;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindTranslationRequest(int documentId,
                                     int selectedWordId,
                                     boolean isPhrase,
                                     @NonNull String targetWord) implements TranslationsRequest, FindOrCreateTranslationRequest {
}
