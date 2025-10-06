@import lule.dictionary.documents.data.documentProcessing.Paragraph
@import lule.dictionary.documents.data.documentProcessing.NewWordUnit
@import lule.dictionary.documents.data.documentProcessing.PhraseUnit
@import lule.dictionary.documents.data.documentProcessing.PersistedWordUnit
@import lule.dictionary.documents.data.response.LoadDocumentResponse
@param LoadDocumentResponse response

<form
hx-trigger="reloadWithSelectedPhrase"
hx-get="/documents/${response.contentData().documentId()}"
hx-swap="outerHTML"
hx-target="#units"
hx-params="startId,endId,phraseText,isPersisted,page,selection"
hx-vals='js:{
    startId: event?.detail?.startId ?? -1,
    endId: event?.detail?.endId ?? -1,
    phraseText: event?.detail?.phraseText ?? "",
    isPersisted: false,
    page: ${response.paginationData().currentPage()},
    selection: "phrase"
}'
id="content"
class="flex flex-row gap-8 text-wrap">
    <div
    class="
    lg:w-3/5
    md:w-4/5
    w-full mx-auto text-wrap flex flex-col gap-4"
    _="
    @raw
    def removeSelection()
        js
            window.getSelection().removeAllRanges()
        end
    end
    on mouseup send selectPhrase
    on touchup send selectPhrase
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
        if the first of phraseNodes's id is the last of phraseNodes's id
            removeSelection()
            throw 'must be at least 2 elements'
        end
        if phraseNodes.length > 5
            removeSelection()
            throw 'cant be longer than 5 elements'
        end
        set newLineCount to 0
        repeat in phraseNodes
            if it@data-raw contains '\n'
                increment newLineCount
            end
        end
        if newLineCount is greater than 1
            removeSelection()
            throw 'cant have more than 1 endline'
        end
        set firstPhraseNode to the first of phraseNodes then
        set lastPhraseNode to the last of phraseNodes then
        set startId to firstPhraseNode.id.slice(5) then
        set endId to lastPhraseNode.id.slice(5) then
        set phraseText to phraseNodes.map( \ node -> node@data-text).join(' ') then
        send reloadWithSelectedPhrase(startId: startId, endId: endId, phraseText: phraseText) to #content
        removeSelection()
    catch e
        removeSelection()
    end
    @endraw
    ">
        @if(response.paginationData().currentPage() == 1)
            <span
            class="
            md:text-4xl
            text-xl text-neutral font-bold ">${response.contentData().title()}</span>
        @endif
        <div
        id="units"
        class="flex flex-col">
            @for(Paragraph paragraph : response.contentData().content())
                <div class="flex flex-wrap flex-row">
                    @for(var unit : paragraph.units())
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
                            startId = unit.id(),
                            isPersisted = true,
                            documentId = response.contentData().documentId(),
                            currentPage = response.paginationData().currentPage(),
                            phraseText = unit.rawText(),
                            translation = phraseUnit.translation(),
                            phraseBgColor = phraseBgColor,
                            phraseTextColor = phraseTextColor,
                            selectable = true)
                        @elseif(unit instanceof PersistedWordUnit)
                            @template.document.word(
                            wordId = unit.id(),
                            bgColor = wordBgColor,
                            gapColor = wordGapColor,
                            filler = wordFiller,
                            textColor = wordTextColor,
                            isPersisted = true,
                            documentId = response.contentData().documentId(),
                            rawText = unit.rawText(),
                            translation = unit.translation(),
                            currentPage = response.paginationData().currentPage())
                        @elseif(unit instanceof NewWordUnit)
                            @if(unit.translation().processedTargetWord().length() <= 50)
                                @template.document.word(
                                wordId = unit.id(),
                                bgColor = wordBgColor,
                                gapColor = wordGapColor,
                                filler = wordFiller,
                                textColor = wordTextColor,
                                isPersisted = false,
                                documentId = response.contentData().documentId(),
                                rawText = unit.rawText(),
                                translation = unit.translation(),
                                currentPage = response.paginationData().currentPage())
                            @endif
                        @else
                            @template.document.word(
                            wordId = unit.id(),
                            bgColor = wordBgColor,
                            gapColor = wordGapColor,
                            filler = wordFiller,
                            textColor = wordTextColor,
                            isSelectable = false,
                            documentId = response.contentData().documentId(),
                            rawText = unit.rawText(),
                            translation = unit.translation(),
                            currentPage = response.paginationData().currentPage(),
                            isPersisted = false)
                        @endif
                    @endfor
                </div>
            @endfor
        </div>
    </div>
    <div
    class="
    md:max-w-2/5
    max-w-full flex flex-col justify-center gap-4  mx-auto items-center select-none">
        @template.document.paginator(
        documentId = response.contentData().documentId(),
        paginationData = response.paginationData())
    </div>
</form>