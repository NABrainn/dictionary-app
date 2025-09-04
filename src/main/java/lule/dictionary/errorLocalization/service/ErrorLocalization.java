package lule.dictionary.errorLocalization.service;

import jakarta.validation.ConstraintViolation;
import lule.dictionary.language.service.Language;

import java.util.Map;

public interface ErrorLocalization {
     Map<String, String> getMessageByViolation(ConstraintViolation<?> violation, Language language);
}
