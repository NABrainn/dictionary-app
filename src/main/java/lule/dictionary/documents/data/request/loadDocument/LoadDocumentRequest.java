package lule.dictionary.documents.data.request.loadDocument;

import lombok.NonNull;

public record LoadDocumentRequest(@NonNull DocumentInfo documentInfo) implements ReadDocumentRequest {
    public static LoadDocumentRequest of(DocumentInfo documentInfo) {
        return new LoadDocumentRequest(documentInfo);
    }
}
