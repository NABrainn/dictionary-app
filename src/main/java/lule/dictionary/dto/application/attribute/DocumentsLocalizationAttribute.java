package lule.dictionary.dto.application.attribute;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record DocumentsLocalizationAttribute(@NonNull String noDocumentsText,
                                             @NonNull String clickHereBtnText,
                                             @NonNull String addFirstText,
                                             @NonNull String wordsTotalText,
                                             @NonNull String newWordsText,
                                             @NonNull String translationsText,
                                             @NonNull String authorText,
                                             @NonNull String addBookBtnText,
                                             @NonNull String toAddYourFirstBtnText) {
}
