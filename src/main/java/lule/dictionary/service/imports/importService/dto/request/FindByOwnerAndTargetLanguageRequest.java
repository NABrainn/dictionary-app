package lule.dictionary.service.imports.importService.dto.request;

import lule.dictionary.dto.application.request.ServiceRequest;
import lule.dictionary.service.language.Language;

public record FindByOwnerAndTargetLanguageRequest(String owner, Language targetLanguage) implements ServiceRequest {
    public static FindByOwnerAndTargetLanguageRequest of(String username, Language language) {
        return new FindByOwnerAndTargetLanguageRequest(username, language);
    }
}
