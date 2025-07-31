@import lule.dictionary.dto.application.BaseAttribute
@import lule.dictionary.dto.application.attribute.NavbarAttribute
@import lule.dictionary.service.imports.importService.dto.request.DocumentContentAttribute
@import lule.dictionary.enumeration.Familiarity
@import gg.jte.support.ForSupport

@param NavbarAttribute navbarAttribute
@param DocumentContentAttribute documentContentAttribute
@param BaseAttribute baseAttribute


    <div
    id="page-content"
    class="flex flex-col gap-8 min-h-full justify-between select-none text-wrap">
        @template.navbar.navbar(
            navbarAttribute = navbarAttribute,
            isAuthenticated = baseAttribute.isAuthenticated(),
            isProfileOpen = false,
            isProfileToggleBtnVisible = false
        )
        <div
        class="
        w-full px-2 min-h-172
        md:w-3/5 mx-auto text-wrap">
            @if(documentContentAttribute.paginationData().currentPageNumber() == 1)
                <span
                class="
                md:text-4xl
                text-xl text-neutral font-bold ">${documentContentAttribute.importData().title()}</span>
            @endif
            <div class="leading-10">
                @for(var paragraphEntry :  ForSupport.of(documentContentAttribute.importData().content().paragraphs()))
                    <div>
                    !{var paragraph = paragraphEntry.get(); var paragraphIndex = paragraphEntry.getIndex();}

                    @for(var wordEntry : ForSupport.of(paragraphEntry.get()))

                        !{var word = wordEntry.get(); var wordIndex = wordEntry.getIndex();}
                        !{var wordId = documentContentAttribute.importData().content().startIndices().get(paragraphIndex) + wordIndex;}
                        !{var cleanWord = word.replaceAll("[^\\p{L}\\p{N}\\s-]", "").replaceAll("-", " ").toLowerCase();}
                        @if(!cleanWord.isEmpty())
                            @if(documentContentAttribute.importData().translations().get(cleanWord) != null)
                                @if(documentContentAttribute.importData().selectedWordId() == wordId)
                                    @if(documentContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.UNKNOWN)
                                        @template.document-page.content.word(
                                            wordIndex = wordId,
                                            importId = documentContentAttribute.importData().importId(),
                                            word = wordEntry.get(),
                                            processedWord = cleanWord,
                                            bgColor = "bg-tertiary bg-accent",
                                            textColor = "text-neutral",
                                            familiarity = documentContentAttribute.importData().translations().get(cleanWord).familiarity(),
                                            found = true,
                                            selected = true
                                        )
                                    @elseif(documentContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.RECOGNIZED)
                                        @template.document-page.content.word(
                                            wordIndex = wordId,
                                            importId = documentContentAttribute.importData().importId(),
                                            word = wordEntry.get(),
                                            processedWord = cleanWord,
                                            bgColor = "bg-tertiary bg-accent/80",
                                            textColor = "text-neutral",
                                            familiarity = documentContentAttribute.importData().translations().get(cleanWord).familiarity(),
                                            found = true,
                                            selected = true
                                        )

                                    @elseif(documentContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.FAMILIAR)
                                        @template.document-page.content.word(
                                            wordIndex = wordId,
                                            importId = documentContentAttribute.importData().importId(),
                                            word = wordEntry.get(),
                                            processedWord = cleanWord,
                                            bgColor = "bg-tertiary bg-accent/60",
                                            textColor = "text-neutral",
                                            familiarity = documentContentAttribute.importData().translations().get(cleanWord).familiarity(),
                                            found = true,
                                            selected = true
                                        )
                                    @elseif(documentContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.KNOWN)
                                        @template.document-page.content.word(
                                            wordIndex = wordId,
                                            importId = documentContentAttribute.importData().importId(),
                                            word = wordEntry.get(),
                                            processedWord = cleanWord,
                                            bgColor = "bg-tertiary bg-primary",
                                            textColor = "text-neutral",
                                            familiarity = documentContentAttribute.importData().translations().get(cleanWord).familiarity(),
                                            found = true,
                                            selected = true
                                        )
                                    @else
                                        @template.document-page.content.word(
                                            wordIndex = wordId,
                                            importId = documentContentAttribute.importData().importId(),
                                            word = wordEntry.get(),
                                            processedWord = cleanWord,
                                            bgColor = "bg-tertiary bg-primary",
                                            textColor = "text-neutral",
                                            familiarity = documentContentAttribute.importData().translations().get(cleanWord).familiarity(),
                                            found = true,
                                            selected = true
                                        )
                                    @endif
                                @else
                                    @if(documentContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.UNKNOWN)
                                        @template.document-page.content.word(
                                            wordIndex = wordId,
                                            importId = documentContentAttribute.importData().importId(),
                                            word = wordEntry.get(),
                                            processedWord = cleanWord,
                                            bgColor = "bg-accent",
                                            textColor = "text-primary",
                                            familiarity = documentContentAttribute.importData().translations().get(cleanWord).familiarity(),
                                            found = true
                                        )
                                    @elseif(documentContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.RECOGNIZED)
                                        @template.document-page.content.word(
                                            wordIndex = wordId,
                                            importId = documentContentAttribute.importData().importId(),
                                            word = wordEntry.get(),
                                            processedWord = cleanWord,
                                            bgColor = "bg-accent/80",
                                            textColor = "text-primary",
                                            familiarity = documentContentAttribute.importData().translations().get(cleanWord).familiarity(),
                                            found = true
                                        )
                                    @elseif(documentContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.FAMILIAR)
                                        @template.document-page.content.word(
                                            wordIndex = wordId,
                                            importId = documentContentAttribute.importData().importId(),
                                            word = wordEntry.get(),
                                            processedWord = cleanWord,
                                            bgColor = "bg-accent/60",
                                            textColor = "text-primary",
                                            familiarity = documentContentAttribute.importData().translations().get(cleanWord).familiarity(),
                                            found = true
                                        )
                                    @elseif(documentContentAttribute.importData().translations().get(cleanWord).familiarity() == Familiarity.KNOWN)
                                        @template.document-page.content.word(
                                            wordIndex = wordId,
                                            importId = documentContentAttribute.importData().importId(),
                                            word = wordEntry.get(),
                                            processedWord = cleanWord,
                                            bgColor = "bg-primary",
                                            textColor = "text-neutral",
                                            familiarity = documentContentAttribute.importData().translations().get(cleanWord).familiarity(),
                                            found = true
                                        )
                                    @else
                                        @template.document-page.content.word(
                                            wordIndex = wordId,
                                            importId = documentContentAttribute.importData().importId(),
                                            word = wordEntry.get(),
                                            processedWord = cleanWord,
                                            bgColor = "bg-primary",
                                            textColor = "text-neutral",
                                            familiarity = documentContentAttribute.importData().translations().get(cleanWord).familiarity(),
                                            found = true
                                        )
                                    @endif
                                @endif
                            @else
                                @if(documentContentAttribute.importData().selectedWordId() == wordId)
                                    @template.document-page.content.word(
                                        wordIndex = wordId,
                                        importId = documentContentAttribute.importData().importId(),
                                        word = wordEntry.get(),
                                        processedWord = cleanWord,
                                        bgColor = "bg-tertiary",
                                        textColor = "text-neutral",
                                        familiarity = null,
                                    )
                                @else
                                    @template.document-page.content.word(
                                        wordIndex = wordId,
                                        importId = documentContentAttribute.importData().importId(),
                                        word = wordEntry.get(),
                                        processedWord = cleanWord,
                                        bgColor = "bg-accent",
                                        textColor = "text-primary",
                                        familiarity = null,
                                    )
                                @endif
                            @endif

                        @else
                            @template.document-page.content.word(
                                wordIndex = wordId,
                                importId = documentContentAttribute.importData().importId(),
                                word = wordEntry.get(),
                                processedWord = cleanWord,
                                bgColor = "bg-primary",
                                textColor = "text-neutral",
                                familiarity = null,
                                selectable = false
                            )
                        @endif
                    @endfor
                    </div>
                @endfor
            </div>
        </div>
        <div
        class="
        md:max-w-2/5 md:gap-4
        max-w-full flex justify-center gap-2  mx-auto items-center">
            @template.document-page.content.paginator(
                id = documentContentAttribute.importData().importId(),
                currentPage = documentContentAttribute.paginationData().currentPageNumber(),
                pages = documentContentAttribute.paginationData().numberOfPages(),
                importId = documentContentAttribute.importData().importId(),
                rows = documentContentAttribute.paginationData().rows(),
                currentRow = documentContentAttribute.paginationData().currentRowNumber(),
                firstPageOfRow = documentContentAttribute.paginationData().firstPageOfRowNumber()
            )
        </div>
    </div>