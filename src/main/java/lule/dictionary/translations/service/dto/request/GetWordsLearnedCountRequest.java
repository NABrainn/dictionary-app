package lule.dictionary.translations.service.dto.request;

import lule.dictionary.shared.ServiceRequest;
import lule.dictionary.language.service.Language;

public record GetWordsLearnedCountRequest(String owner, Language targetLanguage) implements ServiceRequest {
    public static GetWordsLearnedCountRequest of(String username, Language language) {
        return new GetWordsLearnedCountRequest(username, language);
    }
}
