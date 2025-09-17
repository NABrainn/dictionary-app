package lule.dictionary.translations.data.attribute;

import lombok.NonNull;
import lule.dictionary.translations.data.Translation;

import java.util.List;

public record PhrasePartsAttribute(@NonNull List<Translation> translations,
                                   @NonNull List<Integer> ids,
                                   @NonNull List<Boolean> isPersisted) {
    public static @NonNull PhrasePartsAttribute of(List<Translation> phraseParts, List<Integer> ids, List<Boolean> isPersisted) {
        return new PhrasePartsAttribute(phraseParts, ids, isPersisted);
    }
}
