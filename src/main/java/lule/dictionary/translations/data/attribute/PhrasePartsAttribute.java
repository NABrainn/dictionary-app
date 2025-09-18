package lule.dictionary.translations.data.attribute;

import lombok.NonNull;

import java.util.List;

public record PhrasePartsAttribute(@NonNull List<TranslationAttribute> translations) {
    public static PhrasePartsAttribute of(List<TranslationAttribute> translations) {
        return new PhrasePartsAttribute(translations);
    }
}
