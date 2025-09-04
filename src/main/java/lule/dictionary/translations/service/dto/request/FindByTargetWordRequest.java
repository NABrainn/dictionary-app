package lule.dictionary.translations.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lule.dictionary.shared.ServiceRequest;
import lule.dictionary.language.service.Language;

@Builder
public record FindByTargetWordRequest(int documentId,
                                      int selectedWordId,
                                      @NotBlank(message = "Target word cannot be blank")
                                      @Size(max = 200, message = "Target word cannot be longer than 200 characters")
                                      String targetWord,
                                      Language sourceLanguage,
                                      Language targetLanguage,
                                      String owner,
                                      boolean isPhrase) implements ServiceRequest {
}
