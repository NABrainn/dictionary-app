package lule.dictionary.service.translation.dto.request;

import lule.dictionary.dto.application.request.ServiceRequest;
import lule.dictionary.service.language.Language;

public record GetWordsLearnedCountRequest(String owner, Language targetLanguage) implements ServiceRequest {
    public static GetWordsLearnedCountRequest of(String username, Language language) {
        return new GetWordsLearnedCountRequest(username, language);
    }
}
