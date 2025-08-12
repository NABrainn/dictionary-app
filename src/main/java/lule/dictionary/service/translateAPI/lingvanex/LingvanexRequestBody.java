package lule.dictionary.service.translateAPI.lingvanex;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record LingvanexRequestBody(@NonNull String fromLang,
                                   @NonNull String to,
                                   @NonNull String text,
                                   @NonNull String platform) {
}
