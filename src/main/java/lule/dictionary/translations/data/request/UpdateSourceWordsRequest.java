package lule.dictionary.translations.data.request;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.language.service.Language;

import java.util.List;

@Builder
public record UpdateSourceWordsRequest(@NonNull List<String> sourceWords,
                                       @NonNull String targetWord,
                                       @NonNull Familiarity familiarity,
                                       @NonNull Language sourceLanguage,
                                       @NonNull Language targetLanguage,
                                       @NonNull Language systemLanguage,
                                       @NonNull String owner,
                                       int selectedWordId,
                                       boolean isPhrase) implements TranslationsRequest {

    @Override
    public List<String> sourceWords() {
        return List.copyOf(sourceWords);
    }
}
