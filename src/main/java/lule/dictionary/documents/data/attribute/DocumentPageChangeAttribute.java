package lule.dictionary.documents.data.attribute;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.documents.data.DocumentContentData;
import lule.dictionary.pagination.data.DocumentPaginationData;

@Builder
public record DocumentPageChangeAttribute(@NonNull DocumentContentData documentContentData,
                                          @NonNull DocumentPaginationData paginationData,
                                          boolean isNavbarOpen) implements DocumentAttribute {
}
