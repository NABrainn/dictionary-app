package lule.dictionary.documents.data.request.loadDocument;

public record UninitializedSelectedWordDetails(int startId) implements SelectedWordDetails {
    public static SelectedUnitDetails of(int startId) {
        return new UninitializedSelectedWordDetails(startId);
    }
}
