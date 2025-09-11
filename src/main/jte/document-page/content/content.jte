@import lule.dictionary.documents.data.selectable.Word
@import lule.dictionary.documents.data.selectable.Phrase
@import lule.dictionary.documents.data.request.DocumentAttribute
@import gg.jte.support.ForSupport
@import java.util.regex.Pattern

@param DocumentAttribute attribute

!{var translations = attribute.documentContentData().translations();}
!{var content = attribute.documentContentData().content();}
!{var documentId = attribute.documentContentData().documentId();}

<form
id="page-content"
class="flex flex-col gap-8 text-wrap"

data-document-id="${attribute.documentContentData().documentId()}"
hx-trigger="createphrase from:document"
hx-get="/translations/create-phrase"
hx-target="#new-phrase"
hx-swap="outerHTML"
hx-params="selectableId,phraseText,documentId,phraseLength,familiarities,isSavedList"
hx-vals='js:{
    selectableId: data.get(htmx.find("#new-phrase"), "id"),
    phraseText: data.get(htmx.find("#new-phrase"), "value"),
    documentId: ${documentId},
    phraseLength: data.get(htmx.find("#new-phrase"), "length"),
    familiarities: util.findAllByData({ key: "is-wrapped", value: "true" }).filter(word => word.parentElement.id === "new-phrase").map(word => data.get(word, "familiarity")),
    isSavedList: util.findAllByData({ key: "is-wrapped", value: "true" }).filter(word => word.parentElement.id === "new-phrase").map(word => data.get(word, "is-saved") ?? "false"),
}'>
    <div
    id="content-container"
    class="
    w-full px-2
    md:w-3/5 mx-auto text-wrap min-h-165"

    hx-on:mouseup="documentPage.handlePhraseSelection()"
    hx-on::selectphrase="
    documentPage.closeAllTranslationForms()
    documentPage.cleanupSelectedWord()
    documentPage.cleanupSelectedPhrase()">
        @if(attribute.paginationData().currentPageNumber() == 1)
            <span
            id="content-title"
            class="
            md:text-4xl
            text-xl text-neutral font-bold ">${attribute.documentContentData().title()}</span>
        @endif
        <div
        id="content-main"
        class="leading-9 flex flex-col">
            !{var cleanWordPattern = Pattern.compile("[^\\p{L}\\p{N}\\s-]");}
            @for(var paragraphEntry :  ForSupport.of(content))
                !{var paragraph = paragraphEntry.get();}
                <div class="inline-flex flex-wrap">
                    @for(var selectableEntry : ForSupport.of(paragraphEntry.get().selectables()))
                        !{var selectable = selectableEntry.get();}
                        @if(selectable instanceof Word)
                            !{var word = ((Word) selectable).value();}
                            !{var cleanWord = cleanWordPattern.matcher(word).replaceAll("")
                                                    .replace("-", "")
                                                    .toLowerCase();}
                            @if(!cleanWord.isEmpty())
                                @if(translations.get(cleanWord) != null)
                                    !{String wordBgColor = switch(translations.get(cleanWord).familiarity()){
                                        case UNKNOWN -> "bg-accent";
                                        case RECOGNIZED -> "bg-accent/80";
                                        case FAMILIAR -> "bg-accent/60";
                                        default -> "bg-primary";
                                    };}
                                    !{String wordTextColor = switch(translations.get(cleanWord).familiarity()){
                                        case UNKNOWN, RECOGNIZED, FAMILIAR -> "text-primary";
                                        default -> "text-neutral";
                                    };}
                                    @template.document-page.content.word(
                                    wordId = selectable.id(),
                                    processedWord = cleanWord,
                                    documentId = documentId,
                                    word = word,
                                    bgColor = wordBgColor,
                                    textColor = wordTextColor,
                                    familiarity = translations.get(cleanWord).familiarity(),
                                    isFound = true)
                                @else
                                    @template.document-page.content.word(
                                    wordId = selectable.id(),
                                    processedWord = cleanWord,
                                    documentId = documentId,
                                    word = word,
                                    bgColor = "bg-accent",
                                    textColor = "text-primary")
                                @endif
                            @else
                                @template.document-page.content.word(
                                wordId = selectable.id(),
                                processedWord = cleanWord,
                                documentId = documentId,
                                word = word,
                                bgColor = "bg-primary",
                                textColor = "text-neutral",
                                isSelectable = false)
                            @endif
                        @else
                            !{var phrase = ((Phrase) selectable);}
                            @template.document-page.content.update-phrase(
                            phrase = phrase,
                            cleanWordPattern = cleanWordPattern,
                            selectableId = selectable.id(),
                            documentId = documentId,
                            translations = translations)
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
        id = attribute.documentContentData().documentId(),
        currentPage = attribute.paginationData().currentPageNumber(),
        pages = attribute.paginationData().numberOfPages(),
        importId = attribute.documentContentData().documentId(),
        rows = attribute.paginationData().rows(),
        currentRow = attribute.paginationData().currentRowNumber(),
        firstPageOfRow = attribute.paginationData().firstPageOfRowNumber())
    </div>
</form>