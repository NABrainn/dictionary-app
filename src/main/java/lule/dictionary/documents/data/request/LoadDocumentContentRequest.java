package lule.dictionary.documents.data.request;

import jakarta.servlet.http.HttpSession;

public record LoadDocumentContentRequest(int wordId, int documentId, int page, HttpSession session) {
    public static LoadDocumentContentRequest of(int wordId, int importId, int page, HttpSession session) {
        return new LoadDocumentContentRequest(wordId, importId, page, session);
    }
}
