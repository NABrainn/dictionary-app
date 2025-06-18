package lule.dictionary.service.imports.importService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.enumeration.Language;

@Builder
public record AddImportRequest(
        @NotBlank(message = "Title cannot be empty")
        @Size(min = 50, max = 100, message = "Title must be 50 to 100 characters long")
        String title,

        @NotBlank(message = "Content cannot be empty")
        @Size(max = 10000, message = "Content must be no longer than 10000 characters")
        String content,

        @NotNull(message = "Title cannot be null")
        String url,

        @NonNull Language sourceLanguage,
        @NonNull Language targetLanguage,
        @NonNull String owner) {
}
