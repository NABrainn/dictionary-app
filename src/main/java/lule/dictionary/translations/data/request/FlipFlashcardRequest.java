package lule.dictionary.translations.data.request;

import lombok.Builder;

@Builder
public record FlipFlashcardRequest(int id,
                                   int familiarity,
                                   int quantity,
                                   boolean isPhrase) {
}
