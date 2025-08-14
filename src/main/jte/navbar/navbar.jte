@import lule.dictionary.dto.application.attribute.NavbarAttribute

@param NavbarAttribute navbarAttribute
@param boolean isAuthenticated
@param boolean isProfileOpen
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
    gap-2 px-1.5 flex bg-secondary justify-between items-center min-h-18">
        @template.navbar.nav-buttons(
            lessons = navbarAttribute.lessonsBtnText(),
            vocabulary = navbarAttribute.vocabularyBtnText(),
            size = navButtonsSize
        )
        @if(isAuthenticated)
            @if(isProfileToggleBtnVisible)
                @template.navbar.panel-toggle-btn()
            @endif
            @template.navbar.settings(
                text = navbarAttribute.logoutBtnText(),
                size = authButtonsSize,
                languages = navbarAttribute.languageDataList(),
                selectedInterfaceLanguage = ,
                selectedTranslationLanguage =
            )
        @else
            @template.navbar.login-btn(
                text = navbarAttribute.loginBtnText(),
                size = authButtonsSize
            )
        @endif
    </nav>
    @if(isAuthenticated)
        <div
        id="profile-panel">
            @template.navbar.profile-panel(
                navbarAttribute = navbarAttribute,
                isProfileOpen = isProfileOpen
            )
        </div>
    @endif
</div>
