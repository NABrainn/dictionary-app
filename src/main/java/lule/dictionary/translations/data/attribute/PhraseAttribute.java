package lule.dictionary.translations.data.attribute;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record PhraseAttribute(@NonNull TranslationAttribute phraseAttribute,
                              @NonNull PhrasePartsAttribute phrasePartsAttribute,
                              int id,
                              int documentId) {
}
