@import lule.dictionary.dto.application.implementation.imports.DictionaryImport
@import lule.dictionary.enumeration.Language
@import org.springframework.security.web.csrf.CsrfToken
@import java.util.List

@param List<DictionaryImport> imports
@param boolean authenticated
@param Language sourceLanguage
@param Language targetLanguage
@param CsrfToken _csrf 

@template.layout.base(content = @`
    @if(authenticated)
        @if(imports.isEmpty())
            <span class="text-neutral font-bold text-center">There are no books in the library.
                <a class="cursor-pointer text-accent underline"
                hx-get="/imports/new"
                hx-target="#main"
                hx-swap="innerHTML"
                hx-push-url="/imports/new">Click here</a> to add your first.
            </span>
        @endif
        @for(var item : imports)
            ${item.importDetails().title()}
        @endfor
        <span class="text-neutral font-bold text-center">There are no books in the library.
                <a class="cursor-pointer text-accent underline"
                   hx-get="/imports/new"
                   hx-target="#main"
                   hx-swap="innerHTML"
                   hx-push-url="/imports/new">Click here</a> to add your first.
        </span>
    @endif
`, authenticated = authenticated, sourceLanguage = sourceLanguage, targetLanguage = targetLanguage, _csrf = _csrf)
