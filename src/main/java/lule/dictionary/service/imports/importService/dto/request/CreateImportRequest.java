package lule.dictionary.service.imports.importService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NonNull;
import org.hibernate.validator.constraints.URL;


public record CreateImportRequest(@NotBlank(message = "Title cannot be empty")
                                  @Size(min = 10, max = 200, message = "Title must be 10 to 200 characters long")
                                  String title,

                                  @NonNull
                                  @Size(max = 1000000, message = "Content cannot be longer than 1000000 characters")
                                  String content,

                                  @NotNull(message = "Url cannot be null")
                                  @Size(max = 200, message = "URL provided is too long")
                                  @URL(protocol = "https", message = "Invalid URL provided")
                                  String url,

                                  @NotBlank
                                  String owner) {
    public static CreateImportRequest of(String title, String content, String url, @NonNull String username) {
        return new CreateImportRequest(title, content, url, username);
    }
}