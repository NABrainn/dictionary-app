@import lule.dictionary.documents.data.documentProcessing.*
@import lule.dictionary.documents.data.result.ReloadWithPhraseResult
@import lule.dictionary.documents.data.documentProcessing.PhraseUnit
@import lule.dictionary.documents.data.documentProcessing.NonTranslationWordUnit
@import lule.dictionary.documents.data.documentProcessing.SelectedUnit

@param ReloadWithPhraseResult result
<div
id="units"
class="flex flex-col"
_="on load send openForm to #units"
hx-trigger="openForm once"
hx-get="/translations/"
hx-target="#form-container-${result.startId()}"
hx-swap="innerHTML"
hx-params="documentId,targetWord,isPhrase,id,isPersisted"
hx-vals='{
    "documentId": "${result.documentContentData().documentId()}",
    "targetWord": "${result.targetWord()}",
    "isPhrase": "true",
    "id": "${result.startId()}",
    "isPersisted": "${result.isSelectablePersisted()}"
}'>
@for(Paragraph paragraph : result.documentContentData().content())
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
            @if(unit instanceof SelectedUnit selectedUnit)
                @if(selectedUnit instanceof SelectedPhraseUnit selectedPhraseUnit)
                    !{String gapColor = "bg-tertiary";}
                    !{String fillerColor = "bg-tertiary";}
                    !{String textColor = "text-neutral";}
                    @template.document.phrase(
                    phraseId = selectedPhraseUnit.id(),
                    phraseLength = selectedUnit.rawText().split(" ").length,
                    documentId = result.documentContentData().documentId(),
                    currentPage = result.paginationData().currentPageNumber(),
                    phraseText = selectedPhraseUnit.rawText(),
                    translation = selectedPhraseUnit.translation(),
                    phraseBgColor = fillerColor,
                    phraseTextColor = textColor,
                    selectable = false)
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
                phraseId = unit.id(),
                phraseLength = phraseUnit.size(),
                documentId = result.documentContentData().documentId(),
                currentPage = result.paginationData().currentPageNumber(),
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
                isPersisted = false,
                documentId = result.documentContentData().documentId(),
                rawText = unit.rawText(),
                translation = unit.translation(),
                currentPage = result.paginationData().currentPageNumber())
            @elseif(unit instanceof NonTranslationWordUnit)
                @if(unit.translation().processedTargetWord().length() <= 50)
                    @template.document.word(
                    wordId = unit.id(),
                    bgColor = wordBgColor,
                    filler = wordFiller,
                    gapColor = wordGapColor,
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
    @endfor
</div>
