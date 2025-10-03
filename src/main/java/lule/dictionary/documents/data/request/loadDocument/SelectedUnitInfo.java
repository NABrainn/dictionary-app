package lule.dictionary.documents.data.request.loadDocument;

public sealed interface SelectedUnitInfo permits SelectedWordInfo, SelectedPhraseInfo{
    String text();
    boolean isPersisted();
    int startId();
}
