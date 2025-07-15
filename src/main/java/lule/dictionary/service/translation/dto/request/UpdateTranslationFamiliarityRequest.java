package lule.dictionary.service.translation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.dto.application.request.ServiceRequest;
import lule.dictionary.service.imports.importService.dto.FormPositionData;
import lule.dictionary.service.language.Language;

@Builder
public record UpdateTranslationFamiliarityRequest(@NotBlank
                                                  @Size(max = 200, message = "Target word cannot be longer than 200 characters")
                                                  @Pattern(regexp = "^[\\p{L}0-9 ]+$", message = "Source word contains invalid characters")
                                                  String targetWord,
                                                  @NonNull Familiarity familiarity,
                                                  @NonNull Language sourceLanguage,
                                                  @NonNull Language targetLanguage,
                                                  @NonNull String owner,
                                                  int importId,
                                                  int selectedWordId,
                                                  int page,
                                                  @NonNull FormPositionData formPositionData) implements ServiceRequest {
}
