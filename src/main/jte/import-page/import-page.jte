@import lule.dictionary.service.imports.importPageService.dto.ImportModel
@import lule.dictionary.service.language.Language
@import org.springframework.security.web.csrf.CsrfToken
@import java.util.List
@import java.util.Map

@param String username
@param String targetLanguage

@param ImportModel importModel
@param CsrfToken _csrf
@param boolean authenticated
@param int wordsLearned
@param int dailyStreak
@param Map<Language, String> languages

@template.layout.base(content = @`
    @template.import-page.content(
        importModel = importModel,
        _csrf = _csrf,
        authenticated = authenticated,
        username = username,
        targetLanguage = targetLanguage,
        wordsLearned = wordsLearned,
        dailyStreak = dailyStreak,
        languages = languages
    )
    @template.import-page.script(selectedWordId = importModel.selectedWordId())
`,
authenticated = authenticated,
_csrf = _csrf,
username = username,
targetLanguage = targetLanguage,
wordsLearned = wordsLearned,
languages = languages
)

