package lule.dictionary.translations.data.attribute;

import lombok.Builder;

@Builder
public record FlashcardConfigAttribute(int familiarity,
                                       int quantity,
                                       boolean isPhrase) {
}
