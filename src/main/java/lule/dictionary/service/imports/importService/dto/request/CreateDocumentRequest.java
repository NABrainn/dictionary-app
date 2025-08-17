package lule.dictionary.service.imports.importService.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.NonNull;
import org.hibernate.validator.constraints.URL;


public record CreateDocumentRequest(@NotBlank()
                                    @Size(min = 10, max = 200)
                                    String title,

                                    @NonNull
                                    @Size(max = 1000000)
                                    String content,

                                    @NotNull()
                                    @Size(max = 200)
                                    @URL(protocol = "https")
                                    String url,

                                    @NotBlank
                                    String owner) {
    public static CreateDocumentRequest of(String title, String content, String url, @NonNull String username) {
        return new CreateDocumentRequest(title, content, url, username);
    }
}