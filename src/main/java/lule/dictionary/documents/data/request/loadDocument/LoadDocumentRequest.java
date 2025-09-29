package lule.dictionary.documents.data.request.loadDocument;

public sealed interface LoadDocumentRequest permits FirstLoadRequest, ReloadWithWordRequest, ReloadWithPhraseRequest {
    int documentId();
    int page();
    boolean isUnitPersisted();
    String unitText();
}
