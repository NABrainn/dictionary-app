@import lule.dictionary.dto.application.attribute.NavbarAttribute

@param NavbarAttribute navbarAttribute
@param boolean isAuthenticated
@param boolean isProfileOpen
@param boolean isProfileToggleBtnVisible = true
<div>
    <nav
    class="
    md:justify-between md:p-4 md:py-2
    gap-2 justify-between px-1.5 flex bg-secondary items-center min-h-18">
        @template.navbar.home-btn(text = navbarAttribute.homeBtnText())
        @if(isAuthenticated)
            @if(isProfileToggleBtnVisible)
                @template.navbar.panel-toggle-btn()
            @endif
            @template.navbar.logout-btn(
            text = navbarAttribute.logoutBtnText()
        )
        @else
            @template.navbar.login-btn(
                text = navbarAttribute.loginBtnText()
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
