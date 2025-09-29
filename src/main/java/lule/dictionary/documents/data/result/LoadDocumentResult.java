package lule.dictionary.documents.data.result;

import lule.dictionary.documents.data.DocumentContentData;
import lule.dictionary.pagination.data.DocumentPaginationData;

public sealed interface LoadDocumentResult permits FirstLoadResult, PageChangeResult, ReloadWithPhraseResult, ReloadWithWordResult {
    DocumentContentData documentContentData();
    DocumentPaginationData paginationData();
    boolean isNavbarOpen();
}
