package lule.dictionary.documents.data.request.loadDocument;

public record DocumentDetails(int documentId,
                              int page) {
    public static DocumentDetails of(int documentId, int page) {
        return new DocumentDetails(documentId, page);
    }
}
