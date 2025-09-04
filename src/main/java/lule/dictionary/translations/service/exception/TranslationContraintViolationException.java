package lule.dictionary.translations.service.exception;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import lule.dictionary.translations.service.dto.attribute.TranslationAttribute;

@Getter
public class TranslationContraintViolationException extends RuntimeException {

    private final TranslationAttribute translationAttribute;
    private final ConstraintViolation<?> constraintViolation;

    public TranslationContraintViolationException(String message, TranslationAttribute translationAttribute, ConstraintViolation<?> constraintViolation) {
        super(message);
        this.translationAttribute = translationAttribute;
        this.constraintViolation = constraintViolation;
    }

  public TranslationContraintViolationException(TranslationAttribute translationAttribute, ConstraintViolation<?> constraintViolation) {
    this.translationAttribute = translationAttribute;
      this.constraintViolation = constraintViolation;
  }
}
