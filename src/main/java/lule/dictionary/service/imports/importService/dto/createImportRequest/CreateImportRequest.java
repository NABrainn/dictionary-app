package lule.dictionary.service.imports.importService.dto.createImportRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NonNull;
import org.hibernate.validator.constraints.URL;


public record CreateImportRequest(@NotBlank(message = "Title cannot be empty")
                                  @Size(min = 50, max = 150, message = "Title must be 50 to 100 characters long")
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
}