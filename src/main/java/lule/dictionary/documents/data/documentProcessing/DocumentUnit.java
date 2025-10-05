package lule.dictionary.documents.data.documentProcessing;


import lule.dictionary.translations.data.entity.Translation;

public sealed interface DocumentUnit permits PhraseUnit, SelectedUnit, WordUnit {
    int id();
    String rawText();
    Translation translation();
}
