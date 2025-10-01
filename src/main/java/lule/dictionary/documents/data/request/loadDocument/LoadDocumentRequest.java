package lule.dictionary.documents.data.request.loadDocument;

public sealed interface LoadDocumentRequest permits FirstLoadRequest, PageChangeRequest, ReloadWithPhraseRequest, ReloadWithWordRequest {
    int documentId();
    int page();
    boolean isUnitPersisted();
    String unitText();
    int startId();
    int length();
}
