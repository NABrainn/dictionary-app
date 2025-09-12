package lule.dictionary.translations.data.request;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.language.service.Language;
import lule.dictionary.validation.data.Validated;

import java.util.Map;
import java.util.Set;

@Builder
public record FindByTargetWordRequest(int documentId,
                                      int selectedWordId,
                                      String targetWord,
                                      Language sourceLanguage,
                                      Language targetLanguage,
                                      @NonNull Language systemLanguage,
                                      String owner,
                                      boolean isPhrase) implements TranslationsRequest, Validated {
    @Override
    public Set<Map<String, String>> validate(Language language) {
        return Set.of();
    }
}
