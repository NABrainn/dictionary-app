package lule.dictionary.documents.data.response;

import lule.dictionary.documents.data.DocumentContentData;
import lule.dictionary.pagination.data.PaginationData;

public sealed interface ReadDocumentResponse permits LoadDocumentResponse, ReloadWithPhraseResponse, ReloadWithWordResponse {
    DocumentContentData contentData();
    PaginationData paginationData();
}
