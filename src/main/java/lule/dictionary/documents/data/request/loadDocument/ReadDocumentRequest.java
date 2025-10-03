package lule.dictionary.documents.data.request.loadDocument;

public sealed interface ReadDocumentRequest permits LoadDocumentRequest, ReloadDocumentRequest {
    DocumentInfo documentInfo();
}
