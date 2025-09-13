package lule.dictionary.documents.data.request;

import lombok.NonNull;
import lule.dictionary.language.service.Language;

public record FindByTargetLanguageRequest(@NonNull String owner,
                                          @NonNull Language uiLanguage,
                                          @NonNull Language targetLanguage) {
    public static FindByTargetLanguageRequest of(String username, Language uiLanguage, Language targetLanguage) {
        return new FindByTargetLanguageRequest(username, uiLanguage, targetLanguage);
    }
}
