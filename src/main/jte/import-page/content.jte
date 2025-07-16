@import lule.dictionary.dto.application.BaseAttribute
@import lule.dictionary.service.imports.importService.dto.importsAttribute.ImportContentAttribute
@import lule.dictionary.enumeration.Familiarity
@import gg.jte.support.ForSupport
@import lule.dictionary.service.translation.dto.attribute.TranslationAttribute

@param ImportContentAttribute importContentAttribute
@param TranslationAttribute translationAttribute = null
@param BaseAttribute baseAttribute

    <div
    id="page-content"
    class="flex flex-col gap-8 min-h-full justify-between select-none">
        @template.navbar(
            username = baseAttribute.username(),
            authenticated = baseAttribute.isAuthenticated(),
            sourceLanguage = baseAttribute.sourceLanguageData(),
            targetLanguage = baseAttribute.targetLanguageData(),
            wordsLearned = baseAttribute.wordsLearned(),
            dailyStreak = baseAttribute.dailyStreak(),
            languageData = baseAttribute.allLanguageData()
        )
        <div
        class="
        w-full px-2 animate-fade min-h-175
        md:w-3/5 mx-auto">
            @if(importContentAttribute.paginationData().currentPageNumber() == 1)
                <span
                class="
                md:text-4xl
                text-xl text-neutral font-bold ">${importContentAttribute.importData().title()}</span>
            @endif
            <div class="leading-10">
                @for(var word : ForSupport.of(importContentAttribute.importData().content()))
                    !{var cleanWord = word.get().replaceAll("[^\\p{L}\\p{N}]", "").trim().toLowerCase();}
                    @if(!cleanWord.isEmpty())
                        @if(importContentAttribute.importData().translations().get(cleanWord) != null)
                            @if(importContentAttribute.importData().selectedWordId() == word.getIndex())
                                @if(importContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.UNKNOWN)
                                    @template.import-page.word(
                                        wordIndex = word.getIndex(),
                                        importId = importContentAttribute.importData().importId(),
                                        word = word.get(), processedWord = cleanWord,
                                        bgColor = "bg-tertiary bg-accent",
                                        textColor = "text-neutral",
                                        translationAttribute = translationAttribute,
                                        page = importContentAttribute.paginationData().currentPageNumber(),
                                        found = true,
                                        selected = true,
                                    )
                                @elseif(importContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.RECOGNIZED)
                                    @template.import-page.word(
                                        wordIndex = word.getIndex(),
                                        importId = importContentAttribute.importData().importId(),
                                        word = word.get(),
                                        processedWord = cleanWord,
                                        bgColor = "bg-tertiary bg-accent/80",
                                        textColor = "text-neutral",
                                        translationAttribute = translationAttribute,
                                        page = importContentAttribute.paginationData().currentPageNumber(),
                                        found = true,
                                        selected = true,
                                    )

                                @elseif(importContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.FAMILIAR)
                                    @template.import-page.word(
                                        wordIndex = word.getIndex(),
                                        importId = importContentAttribute.importData().importId(),
                                        word = word.get(),
                                        processedWord = cleanWord,
                                        bgColor = "bg-tertiary bg-accent/60",
                                        textColor = "text-neutral",
                                        translationAttribute = translationAttribute,
                                        page = importContentAttribute.paginationData().currentPageNumber(),
                                        found = true,
                                        selected = true,
                                    )
                                @elseif(importContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.KNOWN)
                                    @template.import-page.word(
                                        wordIndex = word.getIndex(),
                                        importId = importContentAttribute.importData().importId(),
                                        word = word.get(),
                                        processedWord = cleanWord,
                                        bgColor = "bg-tertiary bg-primary",
                                        textColor = "text-neutral",
                                        translationAttribute = translationAttribute,
                                        page = importContentAttribute.paginationData().currentPageNumber(),
                                        found = true,
                                        selected = true,
                                    )
                                @else
                                    @template.import-page.word(
                                        wordIndex = word.getIndex(),
                                        importId = importContentAttribute.importData().importId(),
                                        word = word.get(),
                                        processedWord = cleanWord,
                                        bgColor = "bg-tertiary bg-primary",
                                        textColor = "text-neutral",
                                        translationAttribute = translationAttribute,
                                        page = importContentAttribute.paginationData().currentPageNumber(),
                                        found = true,
                                        selected = true,
                                    )
                                @endif
                            @else
                                @if(importContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.UNKNOWN)
                                    @template.import-page.word(
                                        wordIndex = word.getIndex(),
                                        importId = importContentAttribute.importData().importId(),
                                        word = word.get(),
                                        processedWord = cleanWord,
                                        bgColor = "bg-accent",
                                        textColor = "text-primary",
                                        translationAttribute = null,
                                        page = importContentAttribute.paginationData().currentPageNumber(),
                                        found = true
                                    )
                                @elseif(importContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.RECOGNIZED)
                                    @template.import-page.word(
                                        wordIndex = word.getIndex(),
                                        importId = importContentAttribute.importData().importId(),
                                        word = word.get(),
                                        processedWord = cleanWord,
                                        bgColor = "bg-accent/80",
                                        textColor = "text-primary",
                                        translationAttribute = null,
                                        page = importContentAttribute.paginationData().currentPageNumber(),
                                        found = true
                                    )
                                @elseif(importContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.FAMILIAR)
                                    @template.import-page.word(
                                        wordIndex = word.getIndex(),
                                        importId = importContentAttribute.importData().importId(),
                                        word = word.get(),
                                        processedWord = cleanWord,
                                        bgColor = "bg-accent/60",
                                        textColor = "text-primary",
                                        translationAttribute = null,
                                        page = importContentAttribute.paginationData().currentPageNumber(),
                                        found = true
                                    )
                                @elseif(importContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.KNOWN)
                                    @template.import-page.word(
                                        wordIndex = word.getIndex(),
                                        importId = importContentAttribute.importData().importId(),
                                        word = word.get(),
                                        processedWord = cleanWord,
                                        bgColor = "bg-primary",
                                        textColor = "text-neutral",
                                        translationAttribute = null,
                                        page = importContentAttribute.paginationData().currentPageNumber(),
                                        found = true
                                    )
                                @else
                                    @template.import-page.word(
                                        wordIndex = word.getIndex(),
                                        importId = importContentAttribute.importData().importId(),
                                        word = word.get(),
                                        processedWord = cleanWord,
                                        bgColor = "bg-primary",
                                        textColor = "text-neutral",
                                        translationAttribute = null,
                                        page = importContentAttribute.paginationData().currentPageNumber(),
                                        found = true
                                    )
                                @endif
                            @endif
                        @else
                            @if(importContentAttribute.importData().selectedWordId() == word.getIndex())
                                @template.import-page.word(
                                    wordIndex = word.getIndex(),
                                    importId = importContentAttribute.importData().importId(),
                                    word = word.get(),
                                    processedWord = cleanWord,
                                    bgColor = "bg-tertiary",
                                    textColor = "text-neutral",
                                    translationAttribute = null,
                                    page = importContentAttribute.paginationData().currentPageNumber()
                                )
                            @else
                                @template.import-page.word(
                                    wordIndex = word.getIndex(),
                                    importId = importContentAttribute.importData().importId(),
                                    word = word.get(),
                                    processedWord = cleanWord,
                                    bgColor = "bg-accent",
                                    textColor = "text-primary",
                                    translationAttribute = null,
                                    page = importContentAttribute.paginationData().currentPageNumber()
                                )
                            @endif
                        @endif
                    @else
                        @template.import-page.word(
                            wordIndex = word.getIndex(),
                            importId = importContentAttribute.importData().importId(),
                            word = word.get(),
                            processedWord = cleanWord,
                            bgColor = "bg-primary",
                            textColor = "text-neutral",
                            translationAttribute = null,
                            page = importContentAttribute.paginationData().currentPageNumber(),
                            selectable = false
                        )
                    @endif
                @endfor
            </div>
        </div>
        <div
        class="
        md:max-w-2/5 md:gap-4
        max-w-full flex justify-center gap-2  mx-auto items-center">
            @template.import-page.pagination.paginator(
                id = importContentAttribute.importData().importId(),
                currentPage = importContentAttribute.paginationData().currentPageNumber(),
                pages = importContentAttribute.paginationData().numberOfPages(),
                importId = importContentAttribute.importData().importId(),
                rows = importContentAttribute.paginationData().rows(),
                currentRow = importContentAttribute.paginationData().currentRowNumber(),
                firstPageOfRow = importContentAttribute.paginationData().firstPageOfRowNumber()
            )
        </div>
    </div>