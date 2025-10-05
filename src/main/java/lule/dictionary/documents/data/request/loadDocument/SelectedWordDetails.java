package lule.dictionary.documents.data.request.loadDocument;

public sealed interface SelectedWordDetails extends SelectedUnitDetails permits PersistedSelectedWordDetails, UninitializedSelectedWordDetails {
}
