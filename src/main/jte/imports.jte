@import lule.dictionary.entity.application.interfaces.imports.ImportWithId
@import lule.dictionary.enumeration.Language
@import org.springframework.security.web.csrf.CsrfToken
@import java.util.List

@param List<ImportWithId> imports
@param boolean authenticated
@param Language sourceLanguage
@param Language targetLanguage
@param CsrfToken _csrf 

@template.layout.base(content = @`
    @if(authenticated)
        <div class="grow-1 w-3/5 mx-auto flex flex-col justify-between gap-16 text-neutral">
                @if(imports.isEmpty())
                    <div class="flex justify-center">
                            <span class="text-neutral font-bold">There are no books in the library.
                                <a
                                class="cursor-pointer text-accent underline"
                                hx-get="/imports/new"
                                hx-target="#main"
                                hx-swap="innerHTML"
                                hx-push-url="/imports/new">Click here</a> to add your first.
                            </span>
                    </div>
                @else
                    <div class="grid grid-cols-3 gap-8">
                        @for(var item : imports)
                            <div
                            class="flex flex-col gap-2 bg-tertiary rounded p-4 cursor-pointer"
                            hx-get="/imports/${item.id()}?page=1"
                            hx-target="#main"
                            hx-swap="innerHTML"
                            hx-push-url="true">
                                <p class="font-bold text-xl">${item.title()}</p>
                                <span>Url: ${item.url()}</span>
                                <span>Created by:
                                    <span class="text-secondary font-bold">${item.owner()}</span>
                                </span>
                            </div>
                        @endfor
                    </div>
                @endif
                <div class="flex justify-end">
                    <button
                    class="cursor-pointer bg-tertiary px-2 py-1 rounded text-lg font-bold text-neutral"
                    hx-get="/imports/new"
                    hx-target="#main"
                    hx-swap="innerHTML"
                    hx-push-url="/imports/new"
                    type="button">Add book</button>
                </div>
            </div>
    @endif
`, authenticated = authenticated, sourceLanguage = sourceLanguage, targetLanguage = targetLanguage, _csrf = _csrf)
