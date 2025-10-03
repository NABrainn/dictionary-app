package lule.dictionary.documents.data.request.loadDocument;

public sealed interface ReloadDocumentRequest
        extends ReadDocumentRequest
        permits ReloadWithPhrase, ReloadWithWord {
    DocumentInfo documentInfo();
    SelectedUnitInfo selectedUnitInfo();
}
