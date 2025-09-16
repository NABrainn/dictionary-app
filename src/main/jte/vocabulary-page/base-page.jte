@import lule.dictionary.controllerAdvice.data.BaseAttribute
@import lule.dictionary.controllerAdvice.data.NavbarAttribute
@import lule.dictionary.translations.data.attribute.FlashcardConfigAttribute

@param BaseAttribute baseAttribute
@param NavbarAttribute navbarAttribute
@param FlashcardConfigAttribute attribute 


@template.base(content = @`
@template.navbar.navbar(
navbarAttribute = navbarAttribute,
baseAttribute = baseAttribute,
isProfileToggleBtnVisible = false)
@template.vocabulary-page.flashcard.flashcard-config(
attribute = attribute)`,
baseAttribute = baseAttribute)
