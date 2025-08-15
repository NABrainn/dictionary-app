package lule.dictionary.dto.application.attribute;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.dto.application.LanguageData;

import java.util.List;

//TODO separate authenticated data from non-authenticated somehow
@Builder
public record NavbarAttribute(List<LanguageData> languageDataList,
                              LanguageData targetLanguage,
                              LanguageData userInterfaceLanguage,
                              LanguageData translationLanguage,
                              int wordsLearned,
                              int dailyStreak,
                              boolean isProfileOpen,
                              String wordsLearnedText,
                              String daysSingularText,
                              String daysPluralText,
                              String logoutBtnText,
                              @NonNull String loginBtnText,
                              @NonNull String lessonsBtnText,
                              @NonNull String vocabularyBtnText) {

    @Override
    public List<LanguageData> languageDataList() {
        if(languageDataList == null) throw new NullPointerException("Illegal value access: user not authenticated");
        return List.copyOf(languageDataList);
    }

    @Override
    public LanguageData targetLanguage() {
        if(targetLanguage == null) throw new NullPointerException("Illegal value access: user not authenticated");
        return targetLanguage;
    }

    @Override
    public LanguageData userInterfaceLanguage() {
        if(userInterfaceLanguage == null) throw new NullPointerException("Illegal value access: user not authenticated");
        return userInterfaceLanguage;
    }

    @Override
    public LanguageData translationLanguage() {
        if(translationLanguage == null) throw new NullPointerException("Illegal value access: user not authenticated");
        return translationLanguage;
    }

    @Override
    public String wordsLearnedText() {
        if(wordsLearnedText == null) throw new NullPointerException("Illegal value access: user not authenticated");
        return wordsLearnedText;
    }

    @Override
    public String daysSingularText() {
        if(daysSingularText == null) throw new NullPointerException("Illegal value access: user not authenticated");
        return daysSingularText;
    }

    @Override
    public String daysPluralText() {
        if(daysPluralText == null) throw new NullPointerException("Illegal value access: user not authenticated");
        return daysPluralText;
    }

    @Override
    public String logoutBtnText() {
        if(logoutBtnText == null) throw new NullPointerException("Illegal value access: user not authenticated");
        return logoutBtnText;
    }
}
