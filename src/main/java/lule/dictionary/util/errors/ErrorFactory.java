package lule.dictionary.util.errors;

import jakarta.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import lule.dictionary.service.localization.ErrorLocalizationImp;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ErrorFactory {

    private final ErrorLocalizationImp errorLocalization;

    public Map<String, String> fromViolation(ConstraintViolation<?> violation) {

        return Map.of(violation.getPropertyPath().toString(), violation.getMessage());
    }
}
