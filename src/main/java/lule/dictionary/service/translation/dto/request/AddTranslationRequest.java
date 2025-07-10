package lule.dictionary.service.translation.dto.request;

import lombok.Builder;
import lule.dictionary.enumeration.Familiarity;
import lule.dictionary.service.dto.request.ServiceRequest;
import lule.dictionary.service.language.Language;

import java.util.List;

@Builder
public record AddTranslationRequest(int importId,
                                    int selectedWordId,
                                    List<String> sourceWords,
                                    String targetWord,
                                    Language sourceLanguage,
                                    Language targetLanguage,
                                    Familiarity familiarity,
                                    int page,
                                    String owner) implements ServiceRequest {
}
