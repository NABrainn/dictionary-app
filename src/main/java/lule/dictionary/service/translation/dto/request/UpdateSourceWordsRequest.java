package lule.dictionary.service.translation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.NonNull;
import lule.dictionary.service.dto.request.ServiceRequest;

import java.util.List;

public record UpdateSourceWordsRequest(@NonNull
                                       @NotEmpty
                                       List<
                                       @NotBlank(message = "Source word cannot be blank")
                                       @Size(max = 200, message = "Source word cannot be longer than 200 characters")
                                       @Pattern(regexp = "^[\\p{L}0-9 ]+$", message = "Source word contains invalid characters")
                                       String> sourceWords,

                                       @NotBlank(message = "Target word cannot be blank")
                                       @Size(max = 200, message = "Target word cannot be longer than 200 characters")
                                       @Pattern(regexp = "^[\\p{L}0-9 ]+$", message = "Source word contains invalid characters")
                                       String targetWord,

                                       @NonNull
                                       String owner) implements ServiceRequest {

    @Override
    public List<String> sourceWords() {
        return List.copyOf(sourceWords);
    }
}
