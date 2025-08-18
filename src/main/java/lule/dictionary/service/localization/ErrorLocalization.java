package lule.dictionary.service.localization;

import jakarta.validation.ConstraintViolation;
import lule.dictionary.service.language.Language;

import java.util.Map;

public interface ErrorLocalization {
     Map<String, String> getMessageByViolation(ConstraintViolation<?> violation, Language language);
}
