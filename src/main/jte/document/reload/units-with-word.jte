@import lule.dictionary.documents.data.documentProcessing.*
@import lule.dictionary.documents.data.documentProcessing.NonTranslationWordUnit
@import lule.dictionary.documents.data.documentProcessing.PhraseUnit
@import lule.dictionary.documents.data.documentProcessing.SelectedUnit
@import lule.dictionary.documents.data.response.ReloadWithWordResponse

@param ReloadWithWordResponse response
<div
id="units"
class="flex flex-col"
_="on load send openForm to #units"
hx-trigger="openForm once"
hx-get="/translations"
hx-target="#form-container-${response.selectedWordInfo().startId()}"
hx-swap="innerHTML"
hx-params="documentId,targetWord,id,isPersisted"
hx-vals='{
    "documentId": "${response.contentData().documentId()}",
    "targetWord": "${response.selectedWordInfo().text()}",
    "id": "${response.selectedWordInfo().startId()}",
    "isPersisted": "${response.selectedWordInfo().isPersisted()}"
}'>
    @for(Paragraph paragraph : response.contentData().content())
        <div class="flex flex-wrap flex-row">
            @for(var unit : paragraph.units())
                !{String wordBgColor = response.selectedWordInfo().startId() == unit.id() ?
                    "bg-tertiary" :
                    switch(unit.translation().familiarity()){
                        case UNKNOWN -> "bg-accent";
                        case RECOGNIZED -> "bg-accent/80";
                        case FAMILIAR -> "bg-accent/60";
                        default -> "bg-primary";
                    }
                ;}
                !{String wordFiller = response.selectedWordInfo().startId() == unit.id() ?
                    "bg-tertiary" :
                    "primary"
                ;}
                !{String wordTextColor = response.selectedWordInfo().startId() == unit.id() ?
                    "text-neutral" :
                    switch(unit.translation().familiarity()){
                        case UNKNOWN, RECOGNIZED, FAMILIAR -> "text-primary";
                        default -> "text-neutral";
                    }
                ;}
                !{String wordGapColor = response.selectedWordInfo().startId() == unit.id() ?
                    "border-tertiary" :
                    "border-transparent"
                ;}
                !{String phraseGapColor = "border-transparent";}
                @if(unit instanceof SelectedUnit selectedUnit)
                    should be true: ${response.selectedWordInfo().isPersisted()}
                    @if(selectedUnit instanceof SelectedWordUnit selectedWordUnit)
                        @template.document.word(
                        wordId = selectedWordUnit.id(),
                        bgColor = wordBgColor,
                        filler = wordFiller,
                        gapColor = wordGapColor,
                        textColor = wordTextColor,
                        documentId = response.contentData().documentId(),
                        rawText = selectedWordUnit.rawText(),
                        translation = selectedWordUnit.translation(),
                        currentPage = response.paginationData().currentPageNumber(),
                        isPersisted = response.selectedWordInfo().isPersisted())
                    @endif
                @elseif(unit instanceof PhraseUnit phraseUnit)
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
                    endId = phraseUnit.size() - 1,
                    documentId = response.contentData().documentId(),
                    currentPage = response.paginationData().currentPageNumber(),
                    phraseText = unit.rawText(),
                    translation = phraseUnit.translation(),
                    phraseBgColor = phraseBgColor,
                    phraseTextColor = phraseTextColor,
                    selectable = true)
                @elseif(unit instanceof TranslationWordUnit)
                    @template.document.word(
                    wordId = unit.id(),
                    bgColor = wordBgColor,
                    filler = wordFiller,
                    gapColor = wordGapColor,
                    textColor = wordTextColor,
                    isPersisted = true,
                    documentId = response.contentData().documentId(),
                    rawText = unit.rawText(),
                    translation = unit.translation(),
                    currentPage = response.paginationData().currentPageNumber())
                @elseif(unit instanceof NonTranslationWordUnit)
                    @if(unit.translation().processedTargetWord().length() <= 50)
                        @template.document.word(
                        wordId = unit.id(),
                        bgColor = wordBgColor,
                        filler = wordFiller,
                        gapColor = wordGapColor,
                        textColor = wordTextColor,
                        isPersisted = false,
                        documentId = response.contentData().documentId(),
                        rawText = unit.rawText(),
                        translation = unit.translation(),
                        currentPage = response.paginationData().currentPageNumber())
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
                    currentPage = response.paginationData().currentPageNumber(),
                    isPersisted = false)
                @endif
                @endfor
        </div>
    @endfor
</div>
