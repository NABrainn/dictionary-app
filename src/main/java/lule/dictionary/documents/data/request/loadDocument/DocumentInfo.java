package lule.dictionary.documents.data.request.loadDocument;

public record DocumentInfo(int documentId,
                           int page) {
    public static DocumentInfo of(int documentId, int page) {
        return new DocumentInfo(documentId, page);
    }
}
