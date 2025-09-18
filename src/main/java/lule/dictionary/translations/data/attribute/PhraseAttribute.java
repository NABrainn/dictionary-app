package lule.dictionary.translations.data.attribute;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public record PhraseAttribute(@NonNull TranslationAttribute phraseAttribute,
                              @NonNull List<TranslationAttribute> phrasePartsAttribute) {
    public static PhraseAttribute of(TranslationAttribute phraseAttribute, List<TranslationAttribute> phrasePartsAttribute) {
        return new PhraseAttribute(phraseAttribute, phrasePartsAttribute);
    }
}
