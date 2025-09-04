package lule.dictionary.documents.service;

import jakarta.validation.ConstraintViolationException;
import lule.dictionary.documents.data.DocumentWithTranslationData;
import lule.dictionary.documents.service.dto.request.CreateDocumentRequest;
import lule.dictionary.documents.service.dto.request.DocumentAttribute;
import lule.dictionary.documents.service.dto.request.FindByOwnerAndTargetLanguageRequest;
import lule.dictionary.documents.service.dto.request.LoadDocumentContentRequest;

import java.io.IOException;
import java.util.List;

public interface DocumentService {
    int createImport(CreateDocumentRequest createRequest) throws ConstraintViolationException, IOException;
    List<DocumentWithTranslationData> findByOwnerAndTargetLanguage(FindByOwnerAndTargetLanguageRequest request);
    DocumentAttribute loadDocumentContent(LoadDocumentContentRequest loadRequest);
}
