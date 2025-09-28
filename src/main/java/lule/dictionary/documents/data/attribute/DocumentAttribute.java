package lule.dictionary.documents.data.attribute;

import lule.dictionary.documents.data.DocumentContentData;
import lule.dictionary.pagination.data.DocumentPaginationData;

public sealed interface DocumentAttribute permits DocumentFirstLoadAttribute, DocumentPageChangeAttribute, DocumentReloadAttribute {
    DocumentContentData documentContentData();
    DocumentPaginationData paginationData();
    boolean isNavbarOpen();
}
