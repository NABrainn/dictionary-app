@import lule.dictionary.dto.application.BaseAttribute
@import lule.dictionary.dto.application.attribute.NavbarAttribute
@import lule.dictionary.service.imports.importService.dto.Word
@import lule.dictionary.service.imports.importService.dto.Phrase
@import lule.dictionary.service.imports.importService.dto.request.DocumentAttribute
@import lule.dictionary.enumeration.Familiarity
@import gg.jte.support.ForSupport
@import java.util.regex.Pattern

@param NavbarAttribute navbarAttribute
@param DocumentAttribute documentAttribute
@param BaseAttribute baseAttribute

!{var translations = documentAttribute.importData().translations();}
!{var content = documentAttribute.importData().content();}
!{var documentId = documentAttribute.importData().documentId();}

<form
id="page-content"
class="flex flex-col gap-8 min-h-full justify-between text-wrap"
hx-get="/translations/phrase"
hx-trigger="mouseup"
hx-target=".selected-phrase"
hx-swap="outerHTML"
hx-vals='js:{
    targetWords: htmx.find("phrase-selector").data.phraseText,
    selectableId: htmx.find("phrase-selector").data.selectableId
}'
hx-on::after-request="htmx.find('phrase-selector').removeNodes(htmx.find('phrase-selector').data.phraseNodes)"
>
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
        @if(documentAttribute.paginationData().currentPageNumber() == 1)
            <span
            class="
            md:text-4xl
            text-xl text-neutral font-bold ">${documentAttribute.importData().title()}</span>
        @endif
        <div class="leading-10">
            !{var cleanWordPattern = Pattern.compile("[^\\p{L}\\p{N}\\s-]");}
            @for(var paragraphEntry :  ForSupport.of(content.paragraphs()))

                !{var paragraph = paragraphEntry.get(); var paragraphId = paragraphEntry.getIndex();}
                <div
                class="paragraph-${paragraphId}"
                data-paragraph-id="${paragraphId}">
                @for(var selectableEntry : ForSupport.of(paragraphEntry.get()))

                    !{var selectable = selectableEntry.get();}
                    !{var selectableId = selectableEntry.getIndex();}
                    @if(selectable instanceof Word)
                        !{var wordId = content.startIndices().get(paragraphId) + selectableId;}
                        !{var word = ((Word) selectable).value();}
                        !{var cleanWord = cleanWordPattern.matcher(word)
                                                .replaceAll("")
                                                .replace("-", " ")
                                                .toLowerCase();}
                        @if(!cleanWord.isEmpty())
                            @if(translations.get(cleanWord) != null)
                                @if(documentAttribute.importData().selectedWordId() == wordId)
                                    @if(translations.get(cleanWord).familiarity() == Familiarity.UNKNOWN)

                                        @template.document-page.content.word(
                                            wordId = wordId,
                                            processedWord = cleanWord,
                                            documentId = documentId,
                                            word = word,
                                            bgColor = "bg-tertiary bg-accent",
                                            textColor = "text-neutral",
                                            familiarity = translations.get(cleanWord).familiarity()
                                        )
                                    @elseif(translations.get(cleanWord).familiarity() == Familiarity.RECOGNIZED)
                                        @template.document-page.content.word(
                                            wordId = wordId,
                                            processedWord = cleanWord,
                                            documentId = documentId,
                                            word = word,
                                            bgColor = "bg-tertiary bg-accent/80",
                                            textColor = "text-neutral",
                                            familiarity = translations.get(cleanWord).familiarity()
                                        )

                                    @elseif(translations.get(cleanWord).familiarity() == Familiarity.FAMILIAR)
                                        @template.document-page.content.word(
                                            wordId = wordId,
                                            processedWord = cleanWord,
                                            documentId = documentId,
                                            word = word,
                                            bgColor = "bg-tertiary bg-accent/60",
                                            textColor = "text-neutral",
                                            familiarity = translations.get(cleanWord).familiarity()
                                        )
                                    @elseif(translations.get(cleanWord).familiarity() == Familiarity.KNOWN)
                                        @template.document-page.content.word(
                                            wordId = wordId,
                                            processedWord = cleanWord,
                                            documentId = documentId,
                                            word = word,
                                            bgColor = "bg-tertiary bg-primary",
                                            textColor = "text-neutral",
                                            familiarity = translations.get(cleanWord).familiarity()
                                        )
                                    @else
                                        @template.document-page.content.word(
                                            wordId = wordId,
                                            processedWord = cleanWord,
                                            documentId = documentId,
                                            word = word,
                                            bgColor = "bg-tertiary bg-primary",
                                            textColor = "text-neutral",
                                            familiarity = translations.get(cleanWord).familiarity()
                                        )
                                    @endif
                                @else
                                    @if(translations.get(cleanWord).familiarity() == Familiarity.UNKNOWN)

                                        @template.document-page.content.word(
                                            wordId = wordId,
                                            processedWord = cleanWord,
                                            documentId = documentId,
                                            word = word,
                                            bgColor = "bg-accent",
                                            textColor = "text-primary",
                                            familiarity = translations.get(cleanWord).familiarity()
                                        )
                                    @elseif(translations.get(cleanWord).familiarity() == Familiarity.RECOGNIZED)
                                        @template.document-page.content.word(
                                            wordId = wordId,
                                            processedWord = cleanWord,
                                            documentId = documentId,
                                            word = word,
                                            bgColor = "bg-accent/80",
                                            textColor = "text-primary",
                                            familiarity = translations.get(cleanWord).familiarity()
                                        )
                                    @elseif(translations.get(cleanWord).familiarity() == Familiarity.FAMILIAR)
                                        @template.document-page.content.word(
                                            wordId = wordId,
                                            processedWord = cleanWord,
                                            documentId = documentId,
                                            word = word,
                                            bgColor = "bg-accent/60",
                                            textColor = "text-primary",
                                            familiarity = translations.get(cleanWord).familiarity()
                                        )
                                    @elseif(translations.get(cleanWord).familiarity() == Familiarity.KNOWN)
                                        @template.document-page.content.word(
                                            wordId = wordId,
                                            processedWord = cleanWord,
                                            documentId = documentId,
                                            word = word,
                                            bgColor = "bg-primary",
                                            textColor = "text-neutral",
                                            familiarity = translations.get(cleanWord).familiarity()
                                        )
                                    @else
                                        @template.document-page.content.word(
                                            wordId = wordId,
                                            processedWord = cleanWord,
                                            documentId = documentId,
                                            word = word,
                                            bgColor = "bg-primary",
                                            textColor = "text-neutral",
                                            familiarity = translations.get(cleanWord).familiarity()
                                        )
                                    @endif
                                @endif
                            @else
                                @if(documentAttribute.importData().selectedWordId() == wordId)
                                    @template.document-page.content.word(
                                        wordId = wordId,
                                        processedWord = cleanWord,
                                        documentId = documentId,
                                        word = word,
                                        bgColor = "bg-tertiary",
                                        textColor = "text-neutral"
                                    )
                                @else
                                    @template.document-page.content.word(
                                        wordId = wordId,
                                        processedWord = cleanWord,
                                        documentId = documentId,
                                        word = word,
                                        bgColor = "bg-accent",
                                        textColor = "text-primary"
                                    )
                                @endif
                            @endif

                        @else
                            @template.document-page.content.word(
                                wordId = wordId,
                                processedWord = cleanWord,
                                documentId = documentId,
                                word = word,
                                bgColor = "bg-primary",
                                textColor = "text-neutral",
                                selectable = false
                            )
                        @endif
                    @else
                        !{var phrase = ((Phrase) selectable);}
                        @template.document-page.content.update-phrase(
                            phrase = phrase,
                            cleanWordPattern = cleanWordPattern,
                            paragraphId = paragraphId,
                            selectableId = content.startIndices().get(paragraphId) + selectableId,
                            documentId = documentId,
                            startIndices = content.startIndices(),
                            translations = translations
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
            id = documentAttribute.importData().documentId(),
            currentPage = documentAttribute.paginationData().currentPageNumber(),
            pages = documentAttribute.paginationData().numberOfPages(),
            importId = documentAttribute.importData().documentId(),
            rows = documentAttribute.paginationData().rows(),
            currentRow = documentAttribute.paginationData().currentRowNumber(),
            firstPageOfRow = documentAttribute.paginationData().firstPageOfRowNumber()
        )
    </div>
</form>