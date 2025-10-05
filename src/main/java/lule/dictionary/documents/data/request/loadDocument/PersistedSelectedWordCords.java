package lule.dictionary.documents.data.request.loadDocument;

public record PersistedSelectedWordCords(int startId) implements SelectedWordCords{
    public static SelectedUnitCords of(int startId) {
        return new PersistedSelectedWordCords(startId);
    }
}
