package lule.dictionary.validation.service;

import lombok.RequiredArgsConstructor;
import lule.dictionary.language.service.Language;
import lule.dictionary.validation.data.Validated;
import lule.dictionary.validation.data.ValidationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationService {
    public void validate(Validated object, Language language) {
        object.validate(language).stream()
                .findFirst()
                .ifPresent(violation -> { throw new ValidationException(violation); });
    }
}
