@import lule.dictionary.dto.application.BaseAttribute
@import lule.dictionary.dto.application.attribute.DocumentsLocalizationAttribute
@import lule.dictionary.dto.application.attribute.NavbarAttribute
@import lule.dictionary.dto.database.interfaces.imports.ImportWithTranslationData
@import java.util.List

@param NavbarAttribute navbarAttribute
@param BaseAttribute baseAttribute
@param DocumentsLocalizationAttribute documentsLocalizationAttribute
@param List<ImportWithTranslationData> documentsAttribute 


@template.layout.base(content = @`
    @template.navbar.navbar(
        navbarAttribute = navbarAttribute,
        isAuthenticated = baseAttribute.isAuthenticated(),
        isProfileOpen = baseAttribute.isProfileOpen()
    )
    @if(baseAttribute.isAuthenticated())
        <div
        class="md:w-3/5
        w-full grow-1 p-2 mx-auto flex flex-col justify-between gap-16 text-neutral animate-fade">
                @if(documentsAttribute.isEmpty())
                    <div class="flex justify-center">
                        <span class="text-neutral font-bold">${documentsLocalizationAttribute.noDocumentsText()}
                            <a
                            class="cursor-pointer text-accent underline"
                            hx-get="/imports/new"
                            hx-target="#main"
                            hx-swap="innerHTML"
                            hx-push-url="/imports/new">${documentsLocalizationAttribute.clickHereBtnText()}</a> ${documentsLocalizationAttribute.addBookBtnText()}
                        </span>
                    </div>
                @else
                    <div
                    class="md:grid-cols-3
                    grid-cols-1 grid gap-2">
                        @for(var item : documentsAttribute)
                            <div
                            class="flex flex-col gap-2 bg-tertiary rounded p-4 cursor-pointer"
                            hx-get="/imports/${item.id()}?page=1"
                            hx-target="#main"
                            hx-swap="innerHTML"
                            hx-push-url="/imports/${item.id()}">
                                <img
                                class="border min-h-30"
                                src="/images/open-book.svg"
                                alt="image of a document">
                                <p
                                class="md:text-xl
                                text-lg font-bold truncate text-primary">${item.title()}</p>
                                <div class="flex flex-col gap-1">
                                    <span>${documentsLocalizationAttribute.wordsTotalText()}: <span class="text-secondary font-bold">${item.wordCount()}</span></span>
                                    <span>${documentsLocalizationAttribute.newWordsText()}: <span class="text-secondary font-bold">${item.newWordCount()}</span></span>
                                    <span>${documentsLocalizationAttribute.translationsText()}: <span class="text-secondary font-bold">${item.translationCount()}</span></span>
                                    <span>${documentsLocalizationAttribute.authorText()}: <span class="text-secondary font-bold">${item.owner()}</span></span>
                                </div>
                            </div>
                        @endfor
                    </div>
                @endif
                <div class="flex justify-end sticky bottom-8">
                    <button
                    class="cursor-pointer bg-secondary px-2 py-1 rounded text-lg font-bold text-neutral"
                    hx-get="/imports/new"
                    hx-target="#main"
                    hx-swap="innerHTML"
                    hx-push-url="/imports/new"
                    type="button">${documentsLocalizationAttribute.addBookBtnText()}</button>
                </div>
            </div>
    @endif
`,
baseAttribute = baseAttribute)
