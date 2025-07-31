package lule.dictionary.dto.application.attribute;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record DocumentFormAttribute(@NonNull String titleText,
                                    @NonNull String contentText,
                                    @NonNull String importByUrlBtnText,
                                    @NonNull String insertManuallyBtnText,
                                    @NonNull String submitBtnText,
                                    @NonNull String spaceForUrlText,
                                    @NonNull String spaceForContentText) {
}
