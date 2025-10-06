package lule.dictionary.documents.data.entity;

import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.language.service.Language;

@Builder
@Slf4j
public record Document(int id,
                       @NonNull String title,
                       String contentBlob,
                       @NonNull String url,
                       @NonNull Language sourceLanguage,
                       @NonNull Language targetLanguage,
                       @NonNull String owner,
                       int contentLength) {
    @Override
    public String contentBlob() {
        if (contentBlob == null) {
            log.info("Page content is null, returning empty string");
            return "";
        }
        return contentBlob;
    }
}

