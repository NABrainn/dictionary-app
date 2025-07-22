package lule.dictionary.dto.database.implementation.imports.base;

import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lule.dictionary.dto.database.interfaces.imports.base.Import;
import lule.dictionary.service.language.Language;

@Builder(toBuilder = true)
@Slf4j
public record ImportImp(
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
        int totalContentLength) implements Import {

        @Override
        public String pageContent() {
                if (pageContent == null) {
                        log.info("Page content is null, returning empty string");
                        return "";
                }
                return pageContent;
        }
}

