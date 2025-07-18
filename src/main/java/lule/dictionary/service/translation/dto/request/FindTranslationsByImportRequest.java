package lule.dictionary.service.translation.dto.request;

import lule.dictionary.dto.application.request.ServiceRequest;
import lule.dictionary.dto.database.interfaces.imports.base.Import;

public record FindTranslationsByImportRequest(Import anImport, String owner) implements ServiceRequest {
    public static FindTranslationsByImportRequest of(Import anImport, String owner) {
        return new FindTranslationsByImportRequest(anImport, owner);
    }
}
