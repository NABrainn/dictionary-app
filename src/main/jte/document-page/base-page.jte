@import lule.dictionary.controllerAdvice.data.BaseAttribute
@import lule.dictionary.controllerAdvice.data.NavbarAttribute
@import lule.dictionary.documents.data.request.DocumentAttribute

@param NavbarAttribute navbarAttribute
@param DocumentAttribute documentAttribute
@param BaseAttribute baseAttribute
<script src="/js/document-page.js" defer></script>
@template.layout.base(content = @`
    @template.document-page.content.content(
    documentAttribute = documentAttribute,
    baseAttribute = baseAttribute,
    navbarAttribute = NavbarAttribute.builder()
    .wordsLearned(navbarAttribute.wordsLearned())
    .wordsLearnedText(navbarAttribute.wordsLearnedText())
    .daysSingularText(navbarAttribute.daysSingularText())
    .daysPluralText(navbarAttribute.daysPluralText())
    .dailyStreak(navbarAttribute.dailyStreak())
    .isProfileOpen(false)
    .loginBtnText(navbarAttribute.loginBtnText())
    .lessonsBtnText(navbarAttribute.lessonsBtnText())
    .vocabularyBtnText(navbarAttribute.vocabularyBtnText())
    .languageDataList(navbarAttribute.languageDataList())
    .targetLanguage(navbarAttribute.targetLanguage())
    .translationLanguage(navbarAttribute.translationLanguage())
    .userInterfaceLanguage(navbarAttribute.userInterfaceLanguage())
    .settingsText(navbarAttribute.settingsText())
    .languageText(navbarAttribute.languageText())
    .uiText(navbarAttribute.uiText())
    .translationsText(navbarAttribute.translationsText())
    .logoutText(navbarAttribute.logoutText())
    .build())`,
    baseAttribute = baseAttribute)
