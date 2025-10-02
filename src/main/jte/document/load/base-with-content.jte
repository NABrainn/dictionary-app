@import lule.dictionary.controllerAdvice.data.BaseAttribute
@import lule.dictionary.controllerAdvice.data.NavbarAttribute
@import lule.dictionary.documents.data.result.FirstLoadResult

@param NavbarAttribute navbarAttribute
@param BaseAttribute baseAttribute
@param FirstLoadResult result

<script src="/hyperscript.js" defer></script>
<script src="/js/form-positioner.js"></script>

@template.base(content = @`
    @template.navbar.navbar(
    navbarAttribute = navbarAttribute.withNavbarOpen(result.isNavbarOpen()),
    baseAttribute = baseAttribute,
    isProfileToggleBtnVisible = false)
    @template.document.load.content(
    result = result)`,
    baseAttribute = baseAttribute)
