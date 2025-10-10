@import lule.dictionary.controllerAdvice.data.navbar.Navbar
@import lule.dictionary.controllerAdvice.data.user.User
@import lule.dictionary.documents.data.response.LoadDocumentResponse

@param Navbar navbar
@param User user
@param LoadDocumentResponse response

<script src="/hyperscript.js" defer></script>
<script src="/js/form-positioner.js"></script>
@template.base(content = @`
@template.navbar.navbar-without-panel(navbar = navbar)
@template.document.load.content(
response = response)`,
user = user)
