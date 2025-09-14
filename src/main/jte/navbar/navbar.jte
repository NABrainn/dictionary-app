@import lule.dictionary.controllerAdvice.data.BaseAttribute
@import lule.dictionary.controllerAdvice.data.NavbarAttribute

@param BaseAttribute baseAttribute 
@param NavbarAttribute navbarAttribute
@param boolean isProfileToggleBtnVisible = true

!{var navButtonsSize = "";}
!{var authButtonsSize = "";}
@if(isProfileToggleBtnVisible)
    !{navButtonsSize = "basis-2/5";}
    !{authButtonsSize = "basis-2/5";}
@else
    !{navButtonsSize = "basis-1/2";}
    !{authButtonsSize = "basis-1/2";}
@endif
<div>
    <nav
    class="
    md:p-4 md:py-2
    gap-2 px-1.5 flex bg-secondary justify-between items-center min-h-18 select-none">
        @template.navbar.nav-buttons(
        lessons = navbarAttribute.lessonsBtnText(),
        vocabulary = navbarAttribute.vocabularyBtnText(),
        size = navButtonsSize)
        @if(baseAttribute.isAuthenticated())
            @if(isProfileToggleBtnVisible)
                @template.navbar.panel-toggle-btn()
            @endif
            @template.navbar.settings(
            size = authButtonsSize,
            languages = navbarAttribute.languageDataList(),
            userInterfaceLanguage = navbarAttribute.userInterfaceLanguage(),
            translationLanguage = navbarAttribute.translationLanguage(),
            settingsText = navbarAttribute.settingsText(),
            languageText = navbarAttribute.languageText(),
            uiText = navbarAttribute.uiText(),
            translationsText = navbarAttribute.translationsText(),
            logoutText = navbarAttribute.logoutText(),
            _csrf = baseAttribute._csrf().getToken())
        @else
            @template.navbar.login-btn(
            text = navbarAttribute.loginBtnText(),
            size = authButtonsSize)
        @endif
    </nav>
    @if(baseAttribute.isAuthenticated())
        <div
        id="profile-panel">
            @template.navbar.profile-panel(
            navbarAttribute = navbarAttribute,
            isProfileOpen = navbarAttribute.isProfileOpen())
        </div>
    @endif
</div>
