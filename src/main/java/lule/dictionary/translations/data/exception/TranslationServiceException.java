package lule.dictionary.translations.data.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lule.dictionary.translations.data.attribute.TranslationAttribute;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public class TranslationServiceException extends RuntimeException {
    private final TranslationAttribute attribute;
    private final Map<String, String> messages;
}
