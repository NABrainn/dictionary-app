package lule.dictionary.service.translation.dto.request;

import lule.dictionary.dto.application.request.ServiceRequest;
import lule.dictionary.dto.database.interfaces.imports.base.Document;

public record FindTranslationsByImportRequest(Document anImport, String owner) implements ServiceRequest {
    public static FindTranslationsByImportRequest of(Document anImport, String owner) {
        return new FindTranslationsByImportRequest(anImport, owner);
    }
}
