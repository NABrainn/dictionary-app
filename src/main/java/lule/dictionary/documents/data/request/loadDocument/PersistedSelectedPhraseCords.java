package lule.dictionary.documents.data.request.loadDocument;

public record PersistedSelectedPhraseCords(int startId,
                                           int endId) implements SelectedPhraseCords {
    public static PersistedSelectedPhraseCords of(int startId, int endId) {
        return new PersistedSelectedPhraseCords(startId, endId);
    }
}
