package lule.dictionary.translations.data.request;

import lombok.Builder;

@Builder
public record FindByTargetWordRequest(int documentId,
                                      int selectedWordId,
                                      String targetWord,
                                      boolean isPhrase) implements TranslationsRequest {
}
