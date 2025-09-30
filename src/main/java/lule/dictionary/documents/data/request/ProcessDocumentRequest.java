package lule.dictionary.documents.data.request;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.documents.data.documentProcessing.Phrases;
import lule.dictionary.language.service.Language;
import lule.dictionary.translations.data.Translation;

import java.util.Map;

@Builder
public record ProcessDocumentRequest(@NonNull String content,
                                     @NonNull Map<String, Translation> translations,
                                     @NonNull Phrases phrases,
                                     @NonNull Language sourceLanguage,
                                     @NonNull Language targetLanguage,
                                     @NonNull String owner) {
}
