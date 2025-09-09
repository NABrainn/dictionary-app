package lule.dictionary.documents.data.request;

import lombok.NonNull;
import lule.dictionary.translations.data.Translation;

import java.util.List;

public record MapPhrasesRequest(@NonNull String contentBlob,
                                @NonNull List<Translation> phrases) {
    public static MapPhrasesRequest of(String contentBlob, List<Translation> phrases) {
        return new MapPhrasesRequest(contentBlob, phrases);
    }

    @Override
    public List<Translation> phrases() {
        return List.copyOf(phrases);
    }
}
