package lule.dictionary.documents.data.request;

import lombok.NonNull;
import lule.dictionary.documents.data.entity.Document;

public record SanitizeNumberOfPagesRequest(int page,
                                           int numberOfPages,
                                           @NonNull Document document) {
    public static SanitizeNumberOfPagesRequest of(int page, int numberOfPages, Document document) {
        return new SanitizeNumberOfPagesRequest(page, numberOfPages, document);
    }
}
