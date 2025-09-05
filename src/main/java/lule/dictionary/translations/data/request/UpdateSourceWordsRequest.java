package lule.dictionary.translations.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.language.service.Language;

import java.util.List;

@Builder
public record UpdateSourceWordsRequest(@NonNull
                                       List<
                                       @NotBlank(message = "Source word cannot be blank")
                                       @Size(max = 200, message = "Source word cannot be longer than 200 characters")
                                       String> sourceWords,

                                       @NonNull
                                       @NotBlank(message = "Target word cannot be blank")
                                       @Size(max = 200, message = "Target word cannot be longer than 200 characters")
                                       String targetWord,

                                       @NonNull
                                       Familiarity familiarity,

                                       @NonNull
                                       Language sourceLanguage,

                                       @NonNull
                                       Language targetLanguage,

                                       @NonNull
                                       String owner,

                                       int selectedWordId,
                                       boolean isPhrase) implements TranslationsRequest {

    @Override
    public List<String> sourceWords() {
        return List.copyOf(sourceWords);
    }
}
