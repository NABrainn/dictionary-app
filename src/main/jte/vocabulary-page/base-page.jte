@import lule.dictionary.dto.application.BaseAttribute
@import lule.dictionary.dto.application.attribute.NavbarAttribute

@param   BaseAttribute baseAttribute 
@param NavbarAttribute navbarAttribute 


@template.navbar.navbar(
    navbarAttribute = navbarAttribute,
    isAuthenticated = baseAttribute.isAuthenticated(),
    isProfileOpen = false,
    isProfileToggleBtnVisible = false
)