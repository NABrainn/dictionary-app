package lule.dictionary.translations.data.request;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.language.service.Language;

@Builder
public record GetRandomTranslationsRequest(int familiarity,
                                           int quantity,
                                           boolean isPhrase,
                                           int id) {
}
