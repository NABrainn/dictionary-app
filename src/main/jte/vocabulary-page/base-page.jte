@import lule.dictionary.controllerAdvice.data.BaseAttribute
@import lule.dictionary.controllerAdvice.data.NavbarAttribute
@import lule.dictionary.translations.data.TranslationLocalizationKey
@import lule.dictionary.translations.data.attribute.FlashcardConfigAttribute
@import java.util.Map

@param BaseAttribute baseAttribute
@param NavbarAttribute navbarAttribute
@param FlashcardConfigAttribute attribute 
@param Map<TranslationLocalizationKey, String> messages 



@template.base(content = @`
@template.navbar.navbar(
navbarAttribute = navbarAttribute,
baseAttribute = baseAttribute,
isProfileToggleBtnVisible = false)
@template.vocabulary-page.flashcard.flashcard-config(
attribute = attribute,
messages = messages)`,
baseAttribute = baseAttribute)
