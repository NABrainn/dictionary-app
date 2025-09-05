package lule.dictionary.translations.data.request;

import lule.dictionary.language.service.Language;

public record GetWordsLearnedCountRequest(String owner, Language targetLanguage) {
    public static GetWordsLearnedCountRequest of(String username, Language language) {
        return new GetWordsLearnedCountRequest(username, language);
    }
}
