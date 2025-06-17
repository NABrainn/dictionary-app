package lule.dictionary.entity.application.implementation.imports;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.entity.application.interfaces.imports.ImportWithId;
import lule.dictionary.enumeration.Language;

@Builder(toBuilder = true)
public record DictionaryImportWithId(
        @NonNull
        String title,
        @NonNull
        String content,
        @NonNull
        String url,
        @NonNull
        Language sourceLanguage,
        @NonNull
        Language targetLanguage,
        @NonNull
        String owner,
        int id) implements ImportWithId {
}
