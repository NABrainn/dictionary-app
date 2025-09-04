@import lule.dictionary.vocabulary.controller.dto.FlashcardConfigLocalizationAttribute
@import lule.dictionary.controllerAdvice.BaseAttribute
@import lule.dictionary.controllerAdvice.NavbarAttribute

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
