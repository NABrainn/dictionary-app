@import lule.dictionary.controllerAdvice.data.BaseAttribute
@import lule.dictionary.controllerAdvice.data.NavbarAttribute
@import lule.dictionary.documents.data.request.DocumentAttribute

@param NavbarAttribute navbarAttribute
@param BaseAttribute baseAttribute
@param DocumentAttribute attribute

<script src="/js/document-page.js" defer></script>
<script src="/js/form-positioner.js"></script>

@template.base(content = @`
    @template.navbar.navbar(
    navbarAttribute = navbarAttribute.withIsProfileOpen(false),
    baseAttribute = baseAttribute,
    isProfileToggleBtnVisible = false)
    @template.document.content(
    attribute = attribute)`,
    baseAttribute = baseAttribute)
