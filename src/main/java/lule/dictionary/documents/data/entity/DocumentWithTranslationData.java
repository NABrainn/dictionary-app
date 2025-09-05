package lule.dictionary.documents.data.entity;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.language.service.Language;

@Builder
public record DocumentWithTranslationData(int id,
                                          @NonNull
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
                                          int totalContentLength) {
}
