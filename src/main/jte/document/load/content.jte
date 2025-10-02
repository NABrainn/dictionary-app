@import lule.dictionary.documents.data.documentProcessing.DocumentUnit
@import lule.dictionary.documents.data.documentProcessing.NonTranslationWordUnit
@import lule.dictionary.documents.data.documentProcessing.PhraseUnit
@import lule.dictionary.documents.data.documentProcessing.TranslationWordUnit
@import gg.jte.support.ForSupport
@import lule.dictionary.documents.data.result.LoadDocumentResult
@param LoadDocumentResult result

<form
hx-trigger="reloadDocument"
hx-get="/documents/${result.documentContentData().documentId()}/withSelectedPhrase"
hx-swap="outerHTML"
hx-target="#units"
hx-params="startId,length,unitText,isPhrase,isUnitPersisted,page"
hx-vals='js:{
    startId: event?.detail?.startId ?? -1,
    length: event?.detail?.length ?? -1,
    unitText: event?.detail?.phraseText ?? "",
    isPhrase: true,
    isUnitPersisted: false,
    page: ${result.paginationData().currentPageNumber()}
}'
id="content"
class="flex flex-col gap-8 text-wrap">
    <div
    class="
    w-full px-2
    md:w-3/5 mx-auto text-wrap min-h-165"
    _="
    @raw
    def removeSelection()
        js
            window.getSelection().removeAllRanges()
        end
    end
    on mouseup send selectPhrase
    on selectPhrase
        js
            const anchorId = window.getSelection()?.anchorNode?.parentElement?.id?.slice(5) ?? 0
            const focusId = window.getSelection()?.focusNode?.parentElement?.id?.slice(5) ?? 0
            return Array.from(
                { length: focusId - anchorId + 1 },
                (_, i) => htmx.find(`#word-${parseInt(i) + parseInt(anchorId)}`)
            )
        end then
        put it into phraseNodes then
        if the first of phraseNodes.id is the last of phraseNodes.id
            removeSelection()
            throw 'must be at least 2 elements'
        end
        if phraseNodes.length > 5
            removeSelection()
            throw 'cant be longer than 5 elements'
        end
        repeat in phraseNodes
            if it.classList.contains('bg-tertiary')
                removeSelection()
                throw 'cant create phrase from selected unit'
            end
            if it.classList.contains('bg-tertiary')
                removeSelection()
                throw 'cant create phrase from selected unit'
            end
            if it.parentElement.parentElement is 'phrase'
                removeSelection()
                throw 'cant create phrase from another phrase'
            end
            if it.classList.contains('select-none')
                removeSelection()
                throw 'cant create phrase from unselectable unit'
            end
        end
        set firstPhraseNode to the first of phraseNodes then
        set lastPhraseNode to the last of phraseNodes then
        set startId to firstPhraseNode.id.slice(5) then
        set length to phraseNodes.length then
        set phraseText to phraseNodes.map( \ node -> node.innerText).join(' ') then
        send reloadDocument(startId: startId, length: length, phraseText: phraseText) to #content
        removeSelection()
    catch e
        removeSelection()
    end
    @endraw
    ">
        @if(result.paginationData().currentPageNumber() == 1)
            <span
            class="
            md:text-4xl
            text-xl text-neutral font-bold ">${result.documentContentData().title()}</span>
        @endif
        <div
        id="units"
        class="flex flex-wrap">
            @for(var entry :  ForSupport.of(result.documentContentData().content()))
                !{DocumentUnit unit = entry.get();}
                !{String wordBgColor = switch(unit.translation().familiarity()){
                        case UNKNOWN -> "bg-accent";
                        case RECOGNIZED -> "bg-accent/80";
                        case FAMILIAR -> "bg-accent/60";
                        default -> "bg-primary";
                    }
                ;}
                !{String wordFiller = "bg-primary";}
                !{String wordTextColor = switch(unit.translation().familiarity()){
                        case UNKNOWN, RECOGNIZED, FAMILIAR -> "text-primary";
                        default -> "text-neutral";
                    }
                ;}
                !{String wordGapColor = "border-transparent";}
                @if(unit instanceof PhraseUnit phraseUnit)
                    !{String phraseBgColor = switch(phraseUnit.translation().familiarity()){
                        case UNKNOWN -> "bg-accent";
                        case RECOGNIZED -> "bg-accent/80";
                        case FAMILIAR -> "bg-accent/60";
                        default -> "bg-primary";
                    };}
                    !{String phraseTextColor = switch(phraseUnit.translation().familiarity()){
                        case UNKNOWN, RECOGNIZED, FAMILIAR -> "text-primary";
                        default -> "text-neutral";
                    };}
                    @template.document.phrase(
                    phraseId = unit.id(),
                    phraseLength = phraseUnit.size(),
                    documentId = result.documentContentData().documentId(),
                    currentPage = result.paginationData().currentPageNumber(),
                    phraseText = unit.rawText(),
                    translation = phraseUnit.translation(),
                    phraseBgColor = phraseBgColor,
                    phraseTextColor = phraseTextColor,
                    reloader = "phrase-document-reloader-" + unit.id())
                @elseif(unit instanceof TranslationWordUnit)
                    @template.document.word(
                    wordId = unit.id(),
                    bgColor = wordBgColor,
                    gapColor = wordGapColor,
                    filler = wordFiller,
                    textColor = wordTextColor,
                    isPersisted = true,
                    documentId = result.documentContentData().documentId(),
                    rawText = unit.rawText(),
                    translation = unit.translation(),
                    currentPage = result.paginationData().currentPageNumber())
                @elseif(unit instanceof NonTranslationWordUnit)
                    @if(unit.translation().processedTargetWord().length() <= 50)
                        @template.document.word(
                        wordId = unit.id(),
                        bgColor = wordBgColor,
                        gapColor = wordGapColor,
                        filler = wordFiller,
                        textColor = wordTextColor,
                        isPersisted = false,
                        documentId = result.documentContentData().documentId(),
                        rawText = unit.rawText(),
                        translation = unit.translation(),
                        currentPage = result.paginationData().currentPageNumber())
                    @endif
                @else
                    @template.document.word(
                    wordId = unit.id(),
                    bgColor = wordBgColor,
                    gapColor = wordGapColor,
                    filler = wordFiller,
                    textColor = wordTextColor,
                    isSelectable = false,
                    documentId = result.documentContentData().documentId(),
                    rawText = unit.rawText(),
                    translation = unit.translation(),
                    currentPage = result.paginationData().currentPageNumber())
                @endif
            @endfor
        </div>
    </div>
    <div
    class="
    md:max-w-2/5 md:gap-4
    max-w-full flex justify-center gap-2  mx-auto items-center">
        @template.document.paginator(
        id = result.documentContentData().documentId(),
        currentPage = result.paginationData().currentPageNumber(),
        pages = result.paginationData().numberOfPages(),
        rows = result.paginationData().rows(),
        currentRow = result.paginationData().currentRowNumber(),
        firstPageOfRow = result.paginationData().firstPageOfRowNumber())
    </div>
</form>