@import lule.dictionary.controllerAdvice.data.navbar.Navbar
@import lule.dictionary.controllerAdvice.data.navbar.NavbarWithOpenPanel
@import lule.dictionary.controllerAdvice.data.user.User
@import lule.dictionary.translations.data.TranslationLocalizationKey
@import lule.dictionary.translations.data.attribute.FlashcardConfigAttribute
@import java.util.Map

@param User user
@param Navbar navbar
@param FlashcardConfigAttribute attribute 
@param Map<TranslationLocalizationKey, String> messages 

@template.base(content = @`
    @if(navbar instanceof NavbarWithOpenPanel navbarWithOpenPanel)
        @template.navbar.navbar-with-open-panel(navbar = navbar)
    @else
        @template.navbar.navbar-with-closed-panel(navbar = navbar)
    @endif
@template.vocabulary-page.flashcard.flashcard-config(
attribute = attribute,
messages = messages)`,
user = user)
