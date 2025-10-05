package lule.dictionary.documents.data.request.loadDocument;

import lombok.NonNull;

public record LoadDocumentRequest(@NonNull DocumentDetails documentDetails) implements ReadDocumentRequest {
    public static LoadDocumentRequest of(DocumentDetails documentInfo) {
        return new LoadDocumentRequest(documentInfo);
    }
}
