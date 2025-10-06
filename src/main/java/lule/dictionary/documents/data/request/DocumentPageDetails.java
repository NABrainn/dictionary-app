package lule.dictionary.documents.data.request;

import lombok.NonNull;
import lule.dictionary.documents.data.entity.Document;

public record DocumentPageDetails(int page,
                                  int numberOfPages,
                                  @NonNull Document document) {
    public static DocumentPageDetails of(int page, int numberOfPages, Document document) {
        return new DocumentPageDetails(page, numberOfPages, document);
    }
}
