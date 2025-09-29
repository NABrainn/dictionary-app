package lule.dictionary.documents.data.result;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.documents.data.DocumentContentData;
import lule.dictionary.pagination.data.DocumentPaginationData;

@Builder
public record PageChangeResult(@NonNull DocumentContentData documentContentData,
                               @NonNull DocumentPaginationData paginationData,
                               boolean isNavbarOpen) implements LoadDocumentResult {
}
