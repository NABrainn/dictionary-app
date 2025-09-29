package lule.dictionary.translations.data.request;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindTranslationFormRequest(int documentId,
                                         int selectedWordId,
                                         boolean isPhrase,
                                         @NonNull String unprocessedTargetWord) implements TranslationsRequest, GetTranslationFormRequest {
}
