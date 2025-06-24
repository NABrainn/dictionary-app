package lule.dictionary.service.translation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.enumeration.Language;

import java.util.List;

@Builder
public record MutateTranslationRequest(
        @NonNull
        List<
        @NotBlank(message = "Source word cannot be blank")
        @Size(max = 200, message = "Source word cannot be longer than 200 characters")
        @Pattern(regexp = "^[\\p{L}0-9 ]+$", message = "Source word contains invalid characters")
        String
        > sourceWords,

        @NotBlank(message = "Target word cannot be blank")
        @Size(max = 200, message = "Target word cannot be longer than 200 characters")
        @Pattern(regexp = "^[\\p{L}0-9 ]+$", message = "Source word contains invalid characters")
        String targetWord,

        @NonNull
        Familiarity familiarity,
        @NonNull
        Language sourceLanguage,
        @NonNull
        Language targetLanguage,
        @NonNull
        String owner,
        int importId,
        int selectedWordId) {

        @Override
        public List<String> sourceWords() {
                return List.copyOf(sourceWords);
        }
}
