package lule.dictionary.validation.data;

import lule.dictionary.language.service.Language;

import java.util.Map;
import java.util.Set;

public interface Validated {
    Set<Map<String, String>> validate(Language language);
}
