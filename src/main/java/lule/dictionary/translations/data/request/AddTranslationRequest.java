package lule.dictionary.translations.data.request;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.language.service.Language;

import java.util.List;

@Builder
public record AddTranslationRequest(int documentId,
                                    int selectedWordId,
                                    @NonNull List<String> sourceWords,
                                    String targetWord,
                                    @NonNull Language sourceLanguage,
                                    @NonNull Language targetLanguage,
                                    @NonNull Familiarity familiarity,
                                    boolean isPhrase) implements TranslationsRequest {
}
