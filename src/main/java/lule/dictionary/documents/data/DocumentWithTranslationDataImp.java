package lule.dictionary.documents.data;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.language.service.Language;

@Builder
public record DocumentWithTranslationDataImp(@NonNull
                                           String title,
                                             String pageContent,
                                             @NonNull
                                           String url,
                                             @NonNull
                                           Language sourceLanguage,
                                             @NonNull
                                           Language targetLanguage,
                                             @NonNull
                                           String owner,
                                             int wordCount,
                                             int newWordCount,
                                             int translationCount,
                                             int totalContentLength,
                                             int id) implements DocumentWithTranslationData {
}
