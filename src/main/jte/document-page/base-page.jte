@import lule.dictionary.dto.application.BaseAttribute
@import lule.dictionary.dto.application.attribute.NavbarAttribute
@import lule.dictionary.service.imports.importService.dto.request.DocumentAttribute

@param NavbarAttribute navbarAttribute
@param DocumentAttribute documentAttribute
@param BaseAttribute baseAttribute

@template.layout.base(content = @`

    @template.document-page.content.content(
        documentAttribute = documentAttribute,
        baseAttribute = baseAttribute,
        navbarAttribute = navbarAttribute
    )
    @template.document-page.script(selectedWordId = documentAttribute.importData().selectedWordId())
`,
baseAttribute = baseAttribute
)

