package lule.dictionary.documents.data.request;

import lombok.NonNull;
import lule.dictionary.language.service.Language;

public record FindByTargetLanguageRequest(@NonNull String owner,
                                          @NonNull Language sourceLanguage,
                                          @NonNull Language targetLanguage) {
    public static FindByTargetLanguageRequest of(String username, Language sourceLanguage, Language language) {
        return new FindByTargetLanguageRequest(username, sourceLanguage, language);
    }
}
