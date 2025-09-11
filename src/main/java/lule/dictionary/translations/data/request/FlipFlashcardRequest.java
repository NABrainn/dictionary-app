package lule.dictionary.translations.data.request;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.language.service.Language;
import lule.dictionary.translations.data.Translation;
import java.util.List;

@Builder
public record FlipFlashcardRequest(int id,
                                   int size,
                                   int familiarity,
                                   int quantity,
                                   boolean isPhrase,
                                   @NonNull List<Translation> translations,
                                   @NonNull Language systemLanguage) {
}
