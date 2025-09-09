package lule.dictionary.documents.data.request;

public record AssembleDocumentPaginationDataRequest(int totalLength,
                                                    int currentPage) {
    public static AssembleDocumentPaginationDataRequest of(int totalLength, int currentPage) {
        return new AssembleDocumentPaginationDataRequest(totalLength, currentPage);
    }
}
