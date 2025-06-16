package lule.dictionary.service.imports.importService.dto;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.enumeration.Language;

@Builder
public record AddImportRequest(
        @NonNull String title,
        @NonNull String content,
        @NonNull String url,
        @NonNull Language sourceLanguage,
        @NonNull Language targetLanguage,
        @NonNull String owner) {
}
