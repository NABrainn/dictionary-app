@import lule.dictionary.controllerAdvice.data.NavbarLocalizationKey
@import lule.dictionary.controllerAdvice.data.navbar.Navbar
@import lule.dictionary.controllerAdvice.data.user.AuthenticatedUser

@param Navbar navbar

<nav
class="
md:p-4 md:py-2
gap-2 px-1.5 flex bg-secondary justify-between items-center min-h-18 select-none">
    @template.navbar.nav-buttons(
    lessons = navbar.localization().get(NavbarLocalizationKey.LESSONS),
    vocabulary = navbar.localization().get(NavbarLocalizationKey.VOCABULARY))
    @if(navbar.user() instanceof AuthenticatedUser authenticatedUser)
        @template.navbar.progression-panel-toggle-btn()
        @template.navbar.settings(
        languageOptions = navbar.languageOptions(),
        uiLanguage = authenticatedUser.languageSettings().uiLanguage(),
        sourceLanguage = authenticatedUser.languageSettings().sourceLanguage(),
        settingsText = navbar.localization().get(NavbarLocalizationKey.SETTINGS),
        languageText = navbar.localization().get(NavbarLocalizationKey.LANGUAGE),
        uiText = navbar.localization().get(NavbarLocalizationKey.USER_INTERFACE),
        translationsText = navbar.localization().get(NavbarLocalizationKey.TRANSLATIONS),
        logoutText = navbar.localization().get(NavbarLocalizationKey.LOG_OUT),
        csrf = authenticatedUser._csrf())
    @else
        @template.navbar.login-btn(
        text = navbar.localization().get(NavbarLocalizationKey.LOG_IN))
    @endif
</nav>