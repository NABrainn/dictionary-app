@import lule.dictionary.dto.application.BaseAttribute
@import lule.dictionary.dto.database.interfaces.imports.ImportWithTranslationData
@import java.util.List
@import java.util.Map
@import lule.dictionary.dto.application.attribute.ProfilePanelAttribute

@param ProfilePanelAttribute profilePanelAttribute
@param List<ImportWithTranslationData> imports
@param BaseAttribute baseAttribute
@param Map<String, String> navbarLocalization
@param Map<String, String> documentsLocalization


@template.layout.base(content = @`
    @template.navbar.navbar(
        navbarLocalization = navbarLocalization,
        profilePanelAttribute = profilePanelAttribute,
        isAuthenticated = baseAttribute.isAuthenticated(),
        isProfileOpen = baseAttribute.isProfileOpen()
    )
    @if(baseAttribute.isAuthenticated())
        <div
        class="md:w-3/5
        w-full grow-1 p-2 mx-auto flex flex-col justify-between gap-16 text-neutral animate-fade">
                @if(imports.isEmpty())
                    <div class="flex justify-center">
                        <span class="text-neutral font-bold">${documentsLocalization.get("there_are_no_documents_in_the_library")}
                            <a
                            class="cursor-pointer text-accent underline"
                            hx-get="/imports/new"
                            hx-target="#main"
                            hx-swap="innerHTML"
                            hx-push-url="/imports/new">${documentsLocalization.get("click_here")}</a> ${documentsLocalization.get("to_add_your_first")}
                        </span>
                    </div>
                @else
                    <div
                    class="md:grid-cols-3
                    grid-cols-1 grid gap-2">
                        @for(var item : imports)
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
                                    <span>${documentsLocalization.get("words_total")}: <span class="text-secondary font-bold">${item.wordCount()}</span></span>
                                    <span>${documentsLocalization.get("new_words")}: <span class="text-secondary font-bold">${item.newWordCount()}</span></span>
                                    <span>${documentsLocalization.get("translations")}: <span class="text-secondary font-bold">${item.translationCount()}</span></span>
                                    <span>${documentsLocalization.get("author")}: <span class="text-secondary font-bold">${item.owner()}</span></span>
                                </div>
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
                    type="button">${documentsLocalization.get("add_book")}</button>
                </div>
            </div>
    @endif
`,
baseAttribute = baseAttribute)
