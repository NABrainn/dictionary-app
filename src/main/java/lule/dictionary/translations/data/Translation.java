package lule.dictionary.translations.data;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.language.service.Language;

import java.util.List;

@Builder(toBuilder = true)
public record Translation(
        @NonNull
        List<String> sourceWords,
        @NonNull
        String targetWord,
        @NonNull
        Familiarity familiarity,
        @NonNull
        Language sourceLanguage,
        @NonNull
        Language targetLanguage,
        @NonNull
        String owner,
        boolean isPhrase) {

}
