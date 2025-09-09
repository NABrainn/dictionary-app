package lule.dictionary.documents.data.request;

import lule.dictionary.language.service.Language;

public record FindByTargetLanguageRequest(String owner, Language targetLanguage) {
    public static FindByTargetLanguageRequest of(String username, Language language) {
        return new FindByTargetLanguageRequest(username, language);
    }
}
