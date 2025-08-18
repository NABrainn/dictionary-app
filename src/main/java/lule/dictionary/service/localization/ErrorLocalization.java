package lule.dictionary.service.localization;

import jakarta.validation.ConstraintViolation;
import lule.dictionary.service.language.Language;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ErrorLocalization {

    public Map<String, String> documentForm(Language language) {
        List<String> keys = List.of();
        return switch (language) {
            case NO -> constructMap(keys, List.of());
            case IT -> constructMap(keys, List.of());
            case PL -> constructMap(keys, List.of());
            default -> constructMap(keys, List.of());
        };
    }

    private Map<String, String> constructMap(List<String> keys, List<String> values) {
        if (keys.size() != values.size()) {
            throw new IllegalArgumentException("Lists must have equal length: size of keys: " + keys.size() + "; size of values: " + values.size());
        }
        return IntStream.range(0, keys.size())
                .boxed()
                .collect(Collectors.toMap(keys::get, values::get));
    }

    public Map<String, String> getLocalizedMessage(ConstraintViolation<?> violation) {
        return null;
    }
}
