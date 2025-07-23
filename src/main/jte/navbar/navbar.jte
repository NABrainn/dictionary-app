@import java.util.Map
@import lule.dictionary.dto.application.attribute.ProfilePanelAttribute

@param ProfilePanelAttribute profilePanelAttribute
@param Map<String, String> navbarLocalization
@param boolean isAuthenticated
@param boolean isProfileOpen
<nav
class="
md:justify-between md:p-4 md:py-2
gap-2 justify-around px-1 py-1 flex bg-secondary items-center min-h-18">
    @template.navbar.home-btn(text = navbarLocalization.get("home"))
    @if(isAuthenticated)
        <div
        class="flex gap-4 justify-center grow-1"
        id="profile-panel">
            @template.navbar.profile-panel(
                profilePanelAttribute = profilePanelAttribute,
                isProfileOpen = isProfileOpen
            )
        </div>
        @template.navbar.panel-toggle-btn()
    @else
        @template.navbar.login-btn(
            text = navbarLocalization.get("log_in")
        )
    @endif
</nav>