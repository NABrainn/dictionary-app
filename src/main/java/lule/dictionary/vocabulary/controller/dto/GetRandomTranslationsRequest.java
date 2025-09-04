package lule.dictionary.vocabulary.controller.dto;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.shared.ServiceRequest;

@Builder
public record GetRandomTranslationsRequest(int familiarity,
                                           int quantity,
                                           boolean isPhrase,
                                           int id,
                                           @NonNull String owner) implements ServiceRequest {
}
