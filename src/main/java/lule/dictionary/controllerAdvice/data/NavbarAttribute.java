package lule.dictionary.controllerAdvice.data;

import lombok.Builder;
import lombok.NonNull;
import lule.dictionary.language.service.LanguageData;

import java.util.List;

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
                              @NonNull String loginBtnText,
                              @NonNull String lessonsBtnText,
                              @NonNull String vocabularyBtnText,
                              String settingsText,
                              String languageText,
                              String uiText,
                              String translationsText,
                              String logoutText) {

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
    public String settingsText() {
        if(settingsText == null) throw new NullPointerException("Illegal value access: user not authenticated");
        return settingsText;
    }

    @Override
    public String languageText() {
        if(languageText == null) throw new NullPointerException("Illegal value access: user not authenticated");
        return languageText;
    }

    @Override
    public String uiText() {
        if(uiText == null) throw new NullPointerException("Illegal value access: user not authenticated");
        return uiText;
    }

    @Override
    public String translationsText() {
        if(translationsText == null) throw new NullPointerException("Illegal value access: user not authenticated");
        return translationsText;
    }

    @Override
    public String logoutText() {
        if(logoutText == null) throw new NullPointerException("Illegal value access: user not authenticated");
        return logoutText;
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

}
