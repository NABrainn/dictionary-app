package lule.dictionary.controller.vocabularyController.dto;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.dto.application.request.ServiceRequest;

@Builder
public record GetRandomTranslationsRequest(int familiarity,
                                           int quantity,
                                           boolean isPhrase,
                                           int id,
                                           @NonNull String owner) implements ServiceRequest {
}
