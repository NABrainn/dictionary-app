package lule.dictionary.documents.data.request;

import lule.dictionary.language.service.Language;

public record FindByOwnerAndTargetLanguageRequest(String owner, Language targetLanguage) {
    public static FindByOwnerAndTargetLanguageRequest of(String username, Language language) {
        return new FindByOwnerAndTargetLanguageRequest(username, language);
    }
}
