@import lule.dictionary.controllerAdvice.data.navbar.LanguageOption
@import lule.dictionary.controllerAdvice.data.user.LanguageProgression
@import lule.dictionary.language.service.Language
@import java.util.List

@param Language targetLanguage
@param List<LanguageOption> languageOptions
@param LanguageProgression languageProgression
@param String wordsLearnedText
@param String daysSingularText
@param String daysPluralText

<div class="flex bg-secondary justify-center gap-4 pb-4">
    @template.navbar.components.language-picker(
    selectedLanguage = targetLanguage,
    languages = languageOptions,
    type = "target")
    @template.navbar.components.stats(
    wordsLearned = languageProgression.wordsLearned(),
    dailyStreak = languageProgression.dailyStreak(),
    wordsLearnedText = wordsLearnedText,
    daysSingularText = daysSingularText,
    daysPluralText = daysPluralText)
</div>

