package lule.dictionary.translations.service.dto.request;

import lule.dictionary.shared.ServiceRequest;
import lule.dictionary.documents.data.Document;

public record FindTranslationsByImportRequest(Document anImport, String owner) implements ServiceRequest {
    public static FindTranslationsByImportRequest of(Document anImport, String owner) {
        return new FindTranslationsByImportRequest(anImport, owner);
    }
}
