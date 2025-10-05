package lule.dictionary.documents.data.request.loadDocument;

public sealed interface SelectedPhraseDetails extends SelectedUnitDetails permits PersistedSelectedPhraseDetails, UninitializedSelectedPhraseDetails {
    String phraseText();
    int endId();
}
