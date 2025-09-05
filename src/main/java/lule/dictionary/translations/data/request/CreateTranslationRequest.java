package lule.dictionary.translations.data.request;

import lombok.Builder;
import lule.dictionary.language.service.Language;

@Builder
public record CreateTranslationRequest(int documentId,
                                       int selectedWordId,
                                       String username,
                                       boolean isPhrase,
                                       String targetWord,
                                       Language sourceLanguage,
                                       Language targetLanguage) {
}
