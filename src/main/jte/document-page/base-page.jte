@import lule.dictionary.dto.application.BaseAttribute
@import lule.dictionary.dto.application.attribute.NavbarAttribute
@import lule.dictionary.service.imports.importService.dto.request.DocumentAttribute

@param NavbarAttribute navbarAttribute
@param DocumentAttribute documentAttribute
@param BaseAttribute baseAttribute
<script
class="top-8 top-6 top-10 right-4 bottom-12 bottom-10 -right-16 top-4 top-16 bottom-16 bottom-8 left-2 right-2 -left-16 -left-48
border-b-secondary border-l-secondary border-t-secondary border-r-secondary border-l-transparent border-r-transparent border
border-l-2 border-r-2 border-t-2 border-b-2
px-0.5 pl-0.5 pr-0.5 w-[2px] min-w-[2px]"
src="/document-page/script.js"></script>
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

<form-position-adjuster></form-position-adjuster>

