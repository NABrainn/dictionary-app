package lule.dictionary.dto.database.implementation.imports;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.dto.database.interfaces.imports.ImportWithTranslationData;
import lule.dictionary.service.language.Language;

@Builder
public record ImportWithTranslationDataImp(@NonNull
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
                                           int id) implements ImportWithTranslationData {
}
