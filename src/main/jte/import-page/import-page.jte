@import lule.dictionary.service.imports.importPageService.dto.ImportModel
@import org.springframework.security.web.csrf.CsrfToken

@param ImportModel importModel
@param CsrfToken _csrf
@param boolean authenticated

@template.layout.base(content = @`
    @template.import-page.content(importModel = importModel, _csrf = _csrf, authenticated = authenticated)
    @template.import-page.script(selectedWordId = importModel.selectedWordId())
`, authenticated = authenticated, _csrf = _csrf)

