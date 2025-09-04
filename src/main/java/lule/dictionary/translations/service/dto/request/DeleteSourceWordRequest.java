package lule.dictionary.translations.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.shared.ServiceRequest;

@Builder
public record DeleteSourceWordRequest(@NotBlank(message = "Source word cannot be blank")
                                      @Size(max = 200, message = "Source word cannot be longer than 200 characters")
                                      @Pattern(regexp = "^[\\p{L}0-9 ]+$", message = "Source word contains invalid characters")
                                      String sourceWord,

                                      @NotBlank(message = "Target word cannot be blank")
                                      @Size(max = 200, message = "Target word cannot be longer than 200 characters")
                                      String targetWord,

                                      @NonNull
                                      String owner,

                                      int selectedWordId,
                                      boolean isPhrase) implements ServiceRequest {
}
