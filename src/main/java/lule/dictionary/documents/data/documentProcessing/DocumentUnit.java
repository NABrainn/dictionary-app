package lule.dictionary.documents.data.documentProcessing;

import lule.dictionary.translations.data.Translation;

public sealed interface DocumentUnit permits NonTranslationWordUnit, PhraseUnit, TranslationWordUnit {
    String rawText();
    Translation translation();
}
