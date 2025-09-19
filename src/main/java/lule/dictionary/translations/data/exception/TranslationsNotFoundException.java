package lule.dictionary.translations.data.exception;

import lombok.Getter;
import lule.dictionary.translations.data.attribute.FlashcardConfigAttribute;

@Getter
public class TranslationsNotFoundException extends RuntimeException {

    private final FlashcardConfigAttribute attribute;

    public TranslationsNotFoundException(String message, FlashcardConfigAttribute attribute) {
        super(message);
        this.attribute = attribute;
    }
}
