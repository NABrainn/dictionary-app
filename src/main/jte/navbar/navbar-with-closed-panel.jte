@import lule.dictionary.controllerAdvice.data.navbar.Navbar

@param Navbar navbar

!{var localization = navbar.localization();}
<div
id="nav-wrapper">
    @template.navbar.navbar(navbar = navbar)
</div>


