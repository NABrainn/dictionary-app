@import lule.dictionary.dto.application.BaseAttribute
@import lule.dictionary.dto.application.attribute.NavbarAttribute

@param  BaseAttribute baseAttribute
@param NavbarAttribute navbarAttribute 

@template.layout.base(content = @`
    @template.navbar.navbar(
        navbarAttribute = navbarAttribute,
        isAuthenticated = baseAttribute.isAuthenticated(),
        isProfileToggleBtnVisible = false
    )
    @template.vocabulary-page.flashcard.flashcard-config()
`,baseAttribute = baseAttribute)
