package lule.dictionary.documents.data.request.loadDocument;

public sealed interface SelectedUnitInfo permits SelectedWordInfo, SelectedPhraseInfo{
    boolean isPersisted();
    int startId();
}
