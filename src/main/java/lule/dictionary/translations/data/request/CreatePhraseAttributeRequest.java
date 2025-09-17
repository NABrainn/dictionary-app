package lule.dictionary.translations.data.request;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public record CreatePhraseAttributeRequest(@NonNull List<Integer> ids,
                                           @NonNull List<String> unprocessedTargetWords,
                                           @NonNull List<String> familiarities,
                                           @NonNull List<String> isPersistedList,
                                           int id,
                                           int documentId) {
}
