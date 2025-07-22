@import lule.dictionary.dto.application.BaseAttribute
@import lule.dictionary.service.imports.importService.dto.request.ImportContentAttribute
@import java.util.Map
@import lule.dictionary.dto.application.attribute.ProfilePanelAttribute

@param ProfilePanelAttribute profilePanelAttribute
@param ImportContentAttribute importContentAttribute
@param BaseAttribute baseAttribute
@param Map<String, String> navbarLocalization

@template.layout.base(content = @`

    @template.document-page.content.content(
        navbarLocalization = navbarLocalization,
        importContentAttribute = importContentAttribute,
        baseAttribute = baseAttribute,
        profilePanelAttribute = profilePanelAttribute
    )
    @template.document-page.script(selectedWordId = importContentAttribute.importData().selectedWordId())
`,
baseAttribute = baseAttribute
)

