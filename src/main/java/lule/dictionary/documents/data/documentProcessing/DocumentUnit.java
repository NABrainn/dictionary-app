package lule.dictionary.documents.data.documentProcessing;

import lule.dictionary.translations.data.Translation;

public sealed interface DocumentUnit permits PhraseUnit, TranslationUnit, WordUnit {
    String rawText();
    Translation translation();
    boolean isPhrasePart();
}
