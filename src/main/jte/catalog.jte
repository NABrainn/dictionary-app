@import lule.dictionary.dto.application.implementation.imports.DictionaryImport
@import org.springframework.security.core.Authentication
@import java.util.List

@param List<DictionaryImport> imports
@param Authentication authentication

@template.layout.base(content = @`
    @if(authentication != null)
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
            ${item.owner()}
        @endfor
    @endif
`, authentication = authentication)
