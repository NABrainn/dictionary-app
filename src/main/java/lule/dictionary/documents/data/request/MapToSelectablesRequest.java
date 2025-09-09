package lule.dictionary.documents.data.request;

import lombok.NonNull;

import java.util.concurrent.atomic.AtomicInteger;

public record MapToSelectablesRequest(@NonNull String contentBlob,
                                      @NonNull AtomicInteger idCounter) {
    public static MapToSelectablesRequest of(String contentBlob, AtomicInteger idCounter) {
        return new MapToSelectablesRequest(contentBlob, idCounter);
    }
}
