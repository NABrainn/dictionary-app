@import lule.dictionary.dto.application.BaseAttribute
@import lule.dictionary.service.imports.importService.dto.importsAttribute.ImportContentAttribute

@param ImportContentAttribute importContentAttribute
@param BaseAttribute baseAttribute 

@template.layout.base(content = @`
    @template.navbar(
        username = baseAttribute.username(),
        authenticated = baseAttribute.isAuthenticated(),
        sourceLanguage = baseAttribute.sourceLanguageData(),
        targetLanguage = baseAttribute.targetLanguageData(),
        wordsLearned = baseAttribute.wordsLearned(),
        dailyStreak = baseAttribute.dailyStreak(),
        languageData = baseAttribute.allLanguageData()
    )
    @template.import-page.content(
        importContentAttribute = importContentAttribute,
        baseAttribute = baseAttribute
    )
    @template.import-page.script(selectedWordId = importContentAttribute.importData().selectedWordId())
`,
baseAttribute = baseAttribute
)

