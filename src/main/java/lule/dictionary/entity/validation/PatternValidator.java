package lule.dictionary.entity.validation;

import java.util.regex.Pattern;

@FunctionalInterface
public interface PatternValidator {
    void validate(Pattern pattern, String field);
}
