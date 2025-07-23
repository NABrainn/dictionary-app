@import java.util.Map
@import lule.dictionary.dto.application.attribute.ProfilePanelAttribute

@param ProfilePanelAttribute profilePanelAttribute
@param Map<String, String> navbarLocalization
@param boolean isAuthenticated
@param boolean isProfileOpen
@param boolean isProfileToggleBtnVisible = true
<div>
    <nav
    class="
    md:justify-between md:p-4 md:py-2
    gap-2 justify-between px-1.5 flex bg-secondary items-center min-h-18">
        @template.navbar.home-btn(text = navbarLocalization.get("home"))
        @if(isAuthenticated)
            @if(isProfileToggleBtnVisible)
                @template.navbar.panel-toggle-btn()
            @endif
            @template.navbar.logout-btn(
            text = profilePanelAttribute.logoutBtnText()
        )
        @else
            @template.navbar.login-btn(
                text = navbarLocalization.get("log_in")
            )
        @endif
    </nav>
    @if(isAuthenticated)
        <div
        id="profile-panel">
            @template.navbar.profile-panel(
                profilePanelAttribute = profilePanelAttribute,
                isProfileOpen = isProfileOpen
            )
        </div>
    @endif
</div>
