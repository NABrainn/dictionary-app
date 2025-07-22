package lule.dictionary.service.translation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.dto.application.request.ServiceRequest;
import lule.dictionary.service.imports.importService.dto.FormPositionData;
import lule.dictionary.service.language.Language;

@Builder
public record FindByTargetWordRequest(int importId,
                                      int selectedWordId,
                                      @NotBlank(message = "Target word cannot be blank")
                                      @Size(max = 200, message = "Target word cannot be longer than 200 characters")
                                      String targetWord,
                                      Language sourceLanguage,
                                      Language targetLanguage,
                                      String owner) implements ServiceRequest {
}
