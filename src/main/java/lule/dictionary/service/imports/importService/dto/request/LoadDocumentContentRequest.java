package lule.dictionary.service.imports.importService.dto.request;

public record LoadDocumentContentRequest(int wordId, int documentId, int page) {
    public static LoadDocumentContentRequest of(int wordId, int importId, int page) {
        return new LoadDocumentContentRequest(wordId, importId, page);
    }
}
