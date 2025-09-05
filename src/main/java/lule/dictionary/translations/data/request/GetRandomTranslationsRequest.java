package lule.dictionary.translations.data.request;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record GetRandomTranslationsRequest(int familiarity,
                                           int quantity,
                                           boolean isPhrase,
                                           int id,
                                           @NonNull String owner) {
}
