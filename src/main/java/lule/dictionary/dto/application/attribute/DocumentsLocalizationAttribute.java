package lule.dictionary.dto.application.attribute;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.dto.database.interfaces.imports.ImportWithTranslationData;

import java.util.List;

@Builder
public record DocumentsLocalizationAttribute(@NonNull String noDocumentsText,
                                             @NonNull String clickHereBtnText,
                                             @NonNull String addFirstText,
                                             @NonNull String wordsTotalText,
                                             @NonNull String newWordsText,
                                             @NonNull String translationsText,
                                             @NonNull String authorText,
                                             @NonNull String addBookBtnText) {
}
