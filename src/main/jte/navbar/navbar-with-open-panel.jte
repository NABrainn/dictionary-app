@import lule.dictionary.controllerAdvice.data.NavbarLocalizationKey
@import lule.dictionary.controllerAdvice.data.navbar.Navbar
@import lule.dictionary.controllerAdvice.data.user.AuthenticatedUser

@param Navbar navbar

!{var localization = navbar.localization();}
<div
id="nav-wrapper">
    @template.navbar.navbar(navbar = navbar)
    @if(navbar.user() instanceof AuthenticatedUser authenticatedUser)
        @template.navbar.progression-panel(
        targetLanguage = authenticatedUser.languageSettings().targetLanguage(),
        languageOptions = navbar.languageOptions(),
        languageProgression = authenticatedUser.languageProgression(),
        wordsLearnedText = localization.get(NavbarLocalizationKey.WORDS),
        daysSingularText = localization.get(NavbarLocalizationKey.DAYS_SINGULAR),
        daysPluralText = localization.get(NavbarLocalizationKey.DAYS_PLURAL))
    @endif
</div>


