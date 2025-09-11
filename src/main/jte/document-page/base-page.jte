@import lule.dictionary.controllerAdvice.data.BaseAttribute
@import lule.dictionary.controllerAdvice.data.NavbarAttribute
@import lule.dictionary.documents.data.request.DocumentAttribute

@param NavbarAttribute navbarAttribute
@param DocumentAttribute attribute
@param BaseAttribute baseAttribute
<script src="/js/document-page.js" defer></script>

@template.layout.base(content = @`
    @template.navbar.navbar(
    navbarAttribute = navbarAttribute,
    isAuthenticated = baseAttribute.isAuthenticated(),
    isProfileToggleBtnVisible = false)
    @template.document-page.content.content(
    attribute = attribute)`,
    baseAttribute = baseAttribute)
