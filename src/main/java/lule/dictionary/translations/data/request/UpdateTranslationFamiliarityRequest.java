package lule.dictionary.translations.data.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.translations.data.Familiarity;
import lule.dictionary.language.service.Language;

@Builder
public record UpdateTranslationFamiliarityRequest(@NotBlank
                                                  @Size(max = 200, message = "Target word cannot be longer than 200 characters")
                                                  @Pattern(regexp = "^[\\p{L}0-9 ]+$", message = "Source word contains invalid characters")
                                                  String targetWord,
                                                  @NonNull Familiarity familiarity,
                                                  @NonNull Language sourceLanguage,
                                                  @NonNull Language targetLanguage,
                                                  @NonNull String owner,
                                                  int selectedWordId,
                                                  boolean isPhrase) {
}
