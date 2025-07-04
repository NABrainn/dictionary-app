package lule.dictionary.service.translation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FindTranslationRequest(int importId,

                                     @NotBlank(message = "Target word cannot be blank")
                                     @Size(max = 200, message = "Target word cannot be longer than 200 characters")
                                     @Pattern(regexp = "^[\\p{L}0-9 ]+$", message = "Source word contains invalid characters")
                                     String targetWord,

                                     int selectedWordId,
                                     int page) {
}
