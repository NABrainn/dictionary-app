@import lule.dictionary.controllerAdvice.data.BaseAttribute
@import lule.dictionary.controllerAdvice.data.NavbarAttribute
@import lule.dictionary.documents.data.attribute.DocumentFirstLoadAttribute

@param NavbarAttribute navbarAttribute
@param BaseAttribute baseAttribute
@param DocumentFirstLoadAttribute attribute

<script src="/hyperscript.js" defer></script>
<script src="/js/form-positioner.js"></script>

@template.base(content = @`
    @template.navbar.navbar(
    navbarAttribute = navbarAttribute.withIsNavbarOpen(attribute.isNavbarOpen()),
    baseAttribute = baseAttribute,
    isProfileToggleBtnVisible = false)
    @template.document.content(
    attribute = attribute)`,
    baseAttribute = baseAttribute)
