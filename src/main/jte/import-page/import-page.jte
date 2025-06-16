@import lule.dictionary.service.imports.importPageService.dto.ImportPageModel
@import org.springframework.security.web.csrf.CsrfToken

@param ImportPageModel importPageModel
@param CsrfToken _csrf
@param boolean authenticated

@template.layout.base(content = @`
    @template.import-page.content(importPageModel = importPageModel, _csrf = _csrf, authenticated = authenticated)
    @template.import-page.script(selectedWordId = importPageModel.selectedWordId())
`, authenticated = authenticated, _csrf = _csrf)

