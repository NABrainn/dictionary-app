package lule.dictionary.documents.data.entity;

import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.language.service.Language;

@Builder(toBuilder = true)
@Slf4j
public record DocumentImp(
        @NonNull
        String title,
        String pageContent,
        @NonNull
        String url,
        @NonNull
        Language sourceLanguage,
        @NonNull
        Language targetLanguage,
        @NonNull
        String owner,
        int totalContentLength) implements Document {

        @Override
        public String pageContent() {
                if (pageContent == null) {
                        log.info("Page content is null, returning empty string");
                        return "";
                }
                return pageContent;
        }
}

