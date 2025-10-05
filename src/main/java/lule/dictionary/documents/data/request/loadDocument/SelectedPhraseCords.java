package lule.dictionary.documents.data.request.loadDocument;

public sealed interface SelectedPhraseCords extends SelectedUnitCords permits PersistedSelectedPhraseCords, UninitializedSelectedPhraseCords{
    int endId();
}
