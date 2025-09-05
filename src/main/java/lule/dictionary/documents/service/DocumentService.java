package lule.dictionary.documents.service;

import jakarta.validation.ConstraintViolationException;
import lule.dictionary.documents.data.entity.DocumentWithTranslationData;
import lule.dictionary.documents.data.request.CreateDocumentRequest;
import lule.dictionary.documents.data.request.DocumentAttribute;
import lule.dictionary.documents.data.request.FindByOwnerAndTargetLanguageRequest;
import lule.dictionary.documents.data.request.LoadDocumentContentRequest;

import java.io.IOException;
import java.util.List;

public interface DocumentService {
    int createImport(CreateDocumentRequest createRequest) throws ConstraintViolationException, IOException;
    List<DocumentWithTranslationData> findByOwnerAndTargetLanguage(FindByOwnerAndTargetLanguageRequest request);
    DocumentAttribute loadDocumentContent(LoadDocumentContentRequest loadRequest);
}
