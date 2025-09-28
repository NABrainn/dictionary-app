package lule.dictionary.documents.data.attribute;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.documents.data.DocumentContentData;
import lule.dictionary.pagination.data.DocumentPaginationData;

@Builder
public record DocumentReloadAttribute(@NonNull DocumentContentData documentContentData,
                                      @NonNull DocumentPaginationData paginationData,
                                      boolean isNavbarOpen,

                                      int selectedId,
                                      @NonNull String targetWord,
                                      boolean isSelectablePersisted) implements DocumentAttribute {
}
