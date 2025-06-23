package lule.dictionary.service.translation.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record MutateSourceWordListRequest(@NotBlank(message = "source word cannot be blank")
                                          List<String> sourceWord,

                                          @NotBlank(message = "target word cannot be blank")
                                          String targetWord) {
}
