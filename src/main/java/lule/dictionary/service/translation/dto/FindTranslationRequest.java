package lule.dictionary.service.translation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FindTranslationRequest(int importId,

                                     @NotBlank(message = "Target word cannot be blank")
                                     @Size(max = 200, message = "Target word cannot be longer than 200 characters")
                                     String targetWord,

                                     int selectedWordId) {
}
