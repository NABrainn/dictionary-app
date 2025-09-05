package lule.dictionary.translations.data.request;

import lule.dictionary.shared.ServiceRequest;
import lule.dictionary.documents.data.entity.Document;

public record FindTranslationsByImportRequest(Document anImport, String owner) implements ServiceRequest {
    public static FindTranslationsByImportRequest of(Document anImport, String owner) {
        return new FindTranslationsByImportRequest(anImport, owner);
    }
}
