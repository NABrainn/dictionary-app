@import lule.dictionary.dto.application.BaseAttribute
@import lule.dictionary.service.imports.importService.dto.importsAttribute.ImportContentAttribute

@param ImportContentAttribute importContentAttribute
@param BaseAttribute baseAttribute

@template.layout.base(content = @`

    @template.import-page.content(
        importContentAttribute = importContentAttribute,
        baseAttribute = baseAttribute
    )
    @template.import-page.script(selectedWordId = importContentAttribute.importData().selectedWordId())
`,
baseAttribute = baseAttribute
)

