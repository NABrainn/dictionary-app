@import lule.dictionary.controllerAdvice.data.BaseAttribute
@import lule.dictionary.controllerAdvice.data.NavbarAttribute
@import lule.dictionary.documents.data.response.LoadDocumentResponse

@param NavbarAttribute navbarAttribute
@param BaseAttribute baseAttribute
@param LoadDocumentResponse response

<script src="/hyperscript.js" defer></script>
<script src="/js/form-positioner.js"></script>
@template.base(content = @`
    @template.navbar.navbar(
    navbarAttribute = navbarAttribute.withNavbarOpen(response.isNavbarOpen()),
    baseAttribute = baseAttribute,
    isProfileToggleBtnVisible = false)
    @template.document.load.content(
    response = response)`,
    baseAttribute = baseAttribute)
