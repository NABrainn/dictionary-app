@import lule.dictionary.dto.application.BaseAttribute
@import lule.dictionary.dto.application.attribute.NavbarAttribute
@import lule.dictionary.service.imports.importService.dto.request.DocumentContentAttribute

@param NavbarAttribute navbarAttribute
@param DocumentContentAttribute documentContentAttribute
@param BaseAttribute baseAttribute

@template.layout.base(content = @`

    @template.document-page.content.content(
    documentContentAttribute = documentContentAttribute,
        baseAttribute = baseAttribute,
        navbarAttribute = navbarAttribute
    )
    @template.document-page.script(selectedWordId = documentContentAttribute.importData().selectedWordId())
`,
baseAttribute = baseAttribute
)

