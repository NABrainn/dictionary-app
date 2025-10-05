package lule.dictionary.documents.data.request.loadDocument;

public record PersistedSelectedWordDetails(int startId) implements SelectedWordDetails {
    public static SelectedUnitDetails of(int startId) {
        return new PersistedSelectedWordDetails(startId);
    }
}
