package lule.dictionary.translations.data.request;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record DeleteSourceWordRequest(@NonNull String sourceWord,
                                      @NonNull String targetWord,
                                      int selectedWordId,
                                      boolean isPhrase) implements TranslationsRequest {
}
