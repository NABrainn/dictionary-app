package lule.dictionary.dto.application.attribute;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.dto.application.LanguageData;

import java.util.List;

@Builder
public record NavbarAttribute(@NonNull List<LanguageData> languageDataList,
                              @NonNull LanguageData targetLanguage,
                              int wordsLearned,
                              int dailyStreak,
                              @NonNull String wordsLearnedText,
                              @NonNull String daysSingularText,
                              @NonNull String daysPluralText,
                              @NonNull String logoutBtnText,
                              @NonNull String loginBtnText,
                              @NonNull String homeBtnText) {

    @Override
    public List<LanguageData> languageDataList() {
        return List.copyOf(languageDataList);
    }
}
