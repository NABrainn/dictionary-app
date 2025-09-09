package lule.dictionary.documents.data.request;

public record SanitizeNumberOfPagesRequest(int page, int numberOfPages) {
    public static SanitizeNumberOfPagesRequest of(int page, int numberOfPagesForDocument) {
        return new SanitizeNumberOfPagesRequest(page, numberOfPagesForDocument);
    }
}
