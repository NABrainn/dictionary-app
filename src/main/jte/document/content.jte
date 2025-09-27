@import lule.dictionary.documents.data.documentProcessing.DocumentUnit
@import lule.dictionary.documents.data.documentProcessing.PhraseUnit
@import lule.dictionary.documents.data.documentProcessing.TranslationUnit
@import lule.dictionary.documents.data.documentProcessing.WordUnit
@import lule.dictionary.documents.data.request.DocumentAttribute
@import gg.jte.support.ForSupport
@import lule.dictionary.translations.data.Familiarity

@param DocumentAttribute attribute

<form
id="page-content"
class="flex flex-col gap-8 text-wrap"

data-document-id="${attribute.documentContentData().documentId()}"
hx-trigger="swapphrase from:document"
hx-get="/translations/create-phrase"
hx-target="#new-phrase"
hx-swap="outerHTML"
hx-params="selectableId,targetWords,documentId,familiarities,isPersistedList,ids"
hx-vals='js:{
    selectableId: data.get(htmx.find("#new-phrase"), "id"),
    documentId: ${attribute.documentContentData().documentId()},
    familiarities: util.findAllByData({ key: "is-wrapped", value: "true" }).filter(word => word.parentElement.id === "new-phrase").map(word => data.get(word, "familiarity")),
    isPersistedList: util.findAllByData({ key: "is-wrapped", value: "true" }).filter(word => word.parentElement.id === "new-phrase").map(word => data.get(word, "is-saved") ?? "false"),
    ids: util.findAllByData({ key: "is-wrapped", value: "true" }).filter(word => word.parentElement.id === "new-phrase").map(word => data.get(word, "id")),
    targetWords: data.get(htmx.find("#new-phrase"), "value")?.split(" ") ?? []
}'>
    <div
    id="content-container"
    class="
    w-full px-2
    md:w-3/5 mx-auto text-wrap min-h-165"

    hx-on:mouseup="
    documentPage.triggerPhraseSelection((phraseNodes) => this.dispatchEvent(new CustomEvent('selectphrase', { detail: phraseNodes })))"
    hx-on:selectphrase="
    documentPage.closeAllTranslationForms()
    documentPage.cleanupSelectedWord()
    documentPage.cleanupSelectedPhrase()
    documentPage.handlePhraseSelection(event.detail)">
        @if(attribute.paginationData().currentPageNumber() == 1)
            <span
            id="content-title"
            class="
            md:text-4xl
            text-xl text-neutral font-bold ">${attribute.documentContentData().title()}</span>
        @endif
        <div
        id="content-main"
        class="leading-9">
            @for(var entry :  ForSupport.of(attribute.documentContentData().content()))
                !{DocumentUnit unit = entry.get();}
                @if(unit instanceof PhraseUnit)
                    @template.translation.update-phrase(
                    translation = unit.translation(),
                    selectableId = entry.getIndex())
                @elseif(unit instanceof TranslationUnit)
                    !{String wordBgColor = switch(unit.translation().familiarity()){
                        case UNKNOWN -> "bg-accent";
                        case RECOGNIZED -> "bg-accent/80";
                        case FAMILIAR -> "bg-accent/60";
                        default -> "bg-primary";
                    };}
                    !{String wordTextColor = switch(unit.translation().familiarity()){
                        case UNKNOWN, RECOGNIZED, FAMILIAR -> "text-primary";
                        default -> "text-neutral";
                    };}
                    @template.document.word(
                    wordId = entry.getIndex(),
                    processedWord = unit.translation().targetWord(),
                    word = unit.translation().unprocessedTargetWord(),
                    bgColor = wordBgColor,
                    textColor = wordTextColor,
                    familiarity = unit.translation().familiarity(),
                    isPersisted = true)
                @elseif(unit instanceof WordUnit)
                    @if(unit.translation().targetWord().length() <= 50)
                        @template.document.word(
                        wordId = entry.getIndex(),
                        processedWord = unit.translation().targetWord(),
                        word = unit.translation().unprocessedTargetWord(),
                        familiarity = Familiarity.UNKNOWN,
                        bgColor = "bg-accent",
                        textColor = "text-primary")
                    @else
                        @template.document.word(
                        wordId = entry.getIndex(),
                        processedWord = unit.translation().targetWord(),
                        word = unit.translation().unprocessedTargetWord(),
                        familiarity = Familiarity.IGNORED,
                        bgColor = "bg-primary",
                        textColor = "text-neutral",
                        isSelectable = false)
                    @endif
                @else
                    @template.document.word(
                    wordId = entry.getIndex(),
                    processedWord = unit.translation().targetWord(),
                    word = unit.translation().unprocessedTargetWord(),
                    familiarity = Familiarity.IGNORED,
                    bgColor = "bg-primary",
                    textColor = "text-neutral",
                    isSelectable = false)
                @endif
            @endfor
        </div>
    </div>
    <div
    class="
    md:max-w-2/5 md:gap-4
    max-w-full flex justify-center gap-2  mx-auto items-center">
        @template.document.paginator(
        id = attribute.documentContentData().documentId(),
        currentPage = attribute.paginationData().currentPageNumber(),
        pages = attribute.paginationData().numberOfPages(),
        importId = attribute.documentContentData().documentId(),
        rows = attribute.paginationData().rows(),
        currentRow = attribute.paginationData().currentRowNumber(),
        firstPageOfRow = attribute.paginationData().firstPageOfRowNumber())
    </div>
</form>