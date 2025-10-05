package lule.dictionary.documents.data.request.loadDocument;

public record UninitializedSelectedWordCords(int startId) implements SelectedWordCords{
    public static SelectedUnitCords of(int startId) {
        return new UninitializedSelectedWordCords(startId);
    }
}
