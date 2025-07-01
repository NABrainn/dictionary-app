@import lule.dictionary.service.imports.importPageService.dto.ImportModel
@import org.springframework.security.web.csrf.CsrfToken

@param String username
@param String targetLanguage

@param ImportModel importModel
@param CsrfToken _csrf
@param boolean authenticated
@param int wordsLearned

@template.layout.base(content = @`
    @template.import-page.content(importModel = importModel, _csrf = _csrf, authenticated = authenticated)
    @template.import-page.script(selectedWordId = importModel.selectedWordId())
`,
authenticated = authenticated,
_csrf = _csrf,
username = username,
targetLanguage = targetLanguage,
wordsLearned = wordsLearned
)

