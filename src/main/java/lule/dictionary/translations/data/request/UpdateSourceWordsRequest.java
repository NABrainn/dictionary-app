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
public record UpdateSourceWordsRequest(@NonNull List<String> sourceWords,
                                       @NonNull String targetWord,
                                       @NonNull Familiarity familiarity,
                                       @NonNull Language sourceLanguage,
                                       @NonNull Language targetLanguage,
                                       @NonNull Language systemLanguage,
                                       @NonNull String owner,
                                       int selectedWordId,
                                       boolean isPhrase) implements TranslationsRequest, Validated {

    @Override
    public List<String> sourceWords() {
        return List.copyOf(sourceWords);
    }

    @Override
    public Set<Map<String, String>> validate(Language language) {
        return Set.of();
    }
}
