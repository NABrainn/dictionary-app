package lule.dictionary.documents.data.request.loadDocument;

public record UninitializedSelectedPhraseCords(int startId,
                                               int endId) implements SelectedPhraseCords {
    public static SelectedUnitCords of(int startId,
                                       int endId) {
        return new UninitializedSelectedPhraseCords(startId, endId);
    }
}
