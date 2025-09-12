package lule.dictionary.translations.service.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lule.dictionary.translations.data.attribute.TranslationAttribute;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public class TranslationConstraintViolationException extends RuntimeException {
    private final TranslationAttribute translationAttribute;
    private final Map<String, String> violation;

}
