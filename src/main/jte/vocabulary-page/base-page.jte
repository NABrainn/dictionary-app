@import lule.dictionary.translations.controller.vocabulary.dto.FlashcardConfigLocalizationAttribute
@import lule.dictionary.controllerAdvice.data.BaseAttribute
@import lule.dictionary.controllerAdvice.data.NavbarAttribute

@param BaseAttribute baseAttribute
@param NavbarAttribute navbarAttribute 
@param FlashcardConfigLocalizationAttribute flashcardConfigLocalizationAttribute


@template.layout.base(content = @`
    @template.navbar.navbar(
        navbarAttribute = navbarAttribute,
        isAuthenticated = baseAttribute.isAuthenticated(),
        isProfileToggleBtnVisible = false
    )
    @template.vocabulary-page.flashcard.flashcard-config(
        flashcardConfigLocalizationAttribute = flashcardConfigLocalizationAttribute
    )
`, baseAttribute = baseAttribute)
