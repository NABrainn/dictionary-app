@import lule.dictionary.dto.application.BaseAttribute
@import lule.dictionary.dto.application.attribute.NavbarAttribute
@import lule.dictionary.service.imports.importService.dto.request.DocumentAttribute

@param NavbarAttribute navbarAttribute
@param DocumentAttribute documentAttribute
@param BaseAttribute baseAttribute

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
        .build()

    )
    @template.document-page.script(
    selectedWordId = documentAttribute.importData().selectedWordId())
`,
baseAttribute = baseAttribute
)

