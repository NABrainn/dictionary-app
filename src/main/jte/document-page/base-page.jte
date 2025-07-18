@import lule.dictionary.dto.application.BaseAttribute
@import lule.dictionary.service.imports.importService.dto.request.ImportContentAttribute

@param ImportContentAttribute importContentAttribute
@param BaseAttribute baseAttribute

@template.layout.base(content = @`

    @template.document-page.content.content(
        importContentAttribute = importContentAttribute,
        baseAttribute = baseAttribute
    )
    @template.document-page.script(selectedWordId = importContentAttribute.importData().selectedWordId())
`,
baseAttribute = baseAttribute
)

