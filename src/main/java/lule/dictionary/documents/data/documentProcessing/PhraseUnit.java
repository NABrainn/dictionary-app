package lule.dictionary.documents.data.documentProcessing;

import lombok.NonNull;
import lule.dictionary.translations.data.Translation;

import java.util.stream.Collectors;

public record PhraseUnit(@NonNull Translation translation,
                         boolean isPhrasePart) implements DocumentUnit {

    public static DocumentUnit of(Translation translation) {
        return new PhraseUnit(translation, false);
    }
}
