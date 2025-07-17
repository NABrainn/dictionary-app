@import lule.dictionary.dto.application.BaseAttribute
@import lule.dictionary.dto.database.interfaces.imports.ImportWithId
@import java.util.List

@param List<ImportWithId> imports
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
    @if(baseAttribute.isAuthenticated())
        <div
        class="md:w-3/5
        w-full grow-1 p-2 mx-auto flex flex-col justify-between gap-16 text-neutral animate-fade">
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
                    <div
                    class="md:grid-cols-3
                    grid-cols-2 grid gap-2">
                        @for(var item : imports)
                            <div
                            class="flex flex-col gap-2 bg-tertiary rounded p-4 cursor-pointer"
                            hx-get="/imports/${item.id()}?page=1"
                            hx-target="#main"
                            hx-swap="innerHTML"
                            hx-push-url="/imports/${item.id()}">
                                <p
                                class="md:text-xl
                                text-lg font-bold">${item.title()}</p>
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
`,
baseAttribute = baseAttribute)
