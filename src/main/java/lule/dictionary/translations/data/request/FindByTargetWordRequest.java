package lule.dictionary.translations.data.request;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.language.service.Language;

@Builder
public record FindByTargetWordRequest(int documentId,
                                      int selectedWordId,
                                      String targetWord,
                                      Language sourceLanguage,
                                      Language targetLanguage,
                                      @NonNull Language systemLanguage,
                                      String owner,
                                      boolean isPhrase) implements TranslationsRequest {
}
