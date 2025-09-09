package lule.dictionary.translations.data.request;

import lombok.NonNull;

public record FindTranslationsInDocumentRequest(@NonNull String contentBlob, String owner) {
    public static FindTranslationsInDocumentRequest of(@NonNull String contentBlob, String owner) {
        return new FindTranslationsInDocumentRequest(contentBlob, owner);
    }
}
