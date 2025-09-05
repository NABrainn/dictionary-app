package lule.dictionary.translations.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.language.service.Language;

import java.util.List;

@Builder
public record AddTranslationRequest(int documentId,
                                    int selectedWordId,
                                    @NonNull
                                    List<
                                    @NotBlank(message = "Source word cannot be blank")
                                    @Size(max = 200, message = "Source word cannot be longer than 200 characters") String> sourceWords,
                                    @NotBlank
                                    String targetWord,
                                    Language sourceLanguage,
                                    Language targetLanguage,
                                    Familiarity familiarity,
                                    String owner,
                                    boolean isPhrase) implements TranslationsRequest {
}
