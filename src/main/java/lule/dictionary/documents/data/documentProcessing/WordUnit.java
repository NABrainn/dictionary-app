package lule.dictionary.documents.data.documentProcessing;

import lule.dictionary.translations.data.entity.Translation;

public sealed interface WordUnit extends DocumentUnit permits NonTranslationWordUnit, TranslationWordUnit {
    WordUnit withTranslation(Translation translation);
    boolean isPhrasePart();
}
