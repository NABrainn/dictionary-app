package lule.dictionary.translations.data.request;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.language.service.Language;
import lule.dictionary.validation.data.Validated;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Builder
public record AddTranslationRequest(int documentId,
                                    int selectedWordId,
                                    @NonNull List<String> sourceWords,
                                    String targetWord,
                                    @NonNull Language sourceLanguage,
                                    @NonNull Language targetLanguage,
                                    @NonNull Language systemLanguage,
                                    @NonNull Familiarity familiarity,
                                    @NonNull String owner,
                                    boolean isPhrase) implements TranslationsRequest, Validated {
    @Override
    public Set<Map<String, String>> validate(Language language) {
        return Set.of();
    }
}
