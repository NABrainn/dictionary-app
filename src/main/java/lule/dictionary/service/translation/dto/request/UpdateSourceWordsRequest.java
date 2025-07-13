package lule.dictionary.service.translation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.NonNull;
import lule.dictionary.dto.application.request.ServiceRequest;

import java.util.List;

public record UpdateSourceWordsRequest(@NonNull
                                       List<
                                       @NotBlank(message = "Source word cannot be blank")
                                       @Size(max = 200, message = "Source word cannot be longer than 200 characters")
                                       String> sourceWords,

                                       @NotBlank(message = "Target word cannot be blank")
                                       @Size(max = 200, message = "Target word cannot be longer than 200 characters")
                                       String targetWord,

                                       @NonNull
                                       String owner) implements ServiceRequest {

    public static UpdateSourceWordsRequest of(List<String> sourceWords, String targetWord, String username) {
        return new UpdateSourceWordsRequest(sourceWords, targetWord, username);
    }

    @Override
    public List<String> sourceWords() {
        return List.copyOf(sourceWords);
    }
}
