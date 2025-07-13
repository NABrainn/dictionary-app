package lule.dictionary.service.translation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.dto.application.request.ServiceRequest;
import lule.dictionary.service.language.Language;

import java.util.List;

@Builder
public record AddTranslationRequest(int importId,
                                    int selectedWordId,
                                    @NonNull
                                    List<
                                    @NotBlank(message = "Source word cannot be blank")
                                    @Size(max = 200, message = "Source word cannot be longer than 200 characters")
                                    String> sourceWords,
                                    @NotBlank
                                    String targetWord,
                                    Language sourceLanguage,
                                    Language targetLanguage,
                                    Familiarity familiarity,
                                    int page,
                                    String owner) implements ServiceRequest {
}
