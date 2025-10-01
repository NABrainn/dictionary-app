@import lule.dictionary.documents.data.documentProcessing.*
@import lule.dictionary.documents.data.documentProcessing.NonTranslationWordUnit
@import lule.dictionary.documents.data.documentProcessing.PhraseUnit
@import gg.jte.support.ForSupport
@import lule.dictionary.documents.data.result.ReloadWithWordResult
@import lule.dictionary.documents.data.documentProcessing.SelectedUnit

@param ReloadWithWordResult result

<div
id="units"
class="flex inline-flex flex-wrap"
_="on load send openForm to #units"
hx-trigger="openForm once"
hx-get="/translations/"
hx-target="#form-container-${result.unitId()}"
hx-swap="innerHTML"
hx-params="documentId,targetWord,id,isPersisted"
hx-vals='{
    "documentId": "${result.documentContentData().documentId()}",
    "targetWord": "${result.targetWord()}",
    "id": "${result.unitId()}",
    "isPersisted": "${result.isSelectablePersisted()}"
}'>
    @for(var entry :  ForSupport.of(result.documentContentData().content()))
        !{DocumentUnit unit = entry.get();}
        !{String wordBgColor = result.unitId() == entry.getIndex() ?
            "bg-tertiary" :
            switch(unit.translation().familiarity()){
                case UNKNOWN -> "bg-accent";
                case RECOGNIZED -> "bg-accent/80";
                case FAMILIAR -> "bg-accent/60";
                default -> "bg-primary";
            }
        ;}
        !{String wordFiller = result.unitId() == entry.getIndex() ?
            "bg-tertiary" :
            "primary"
        ;}
        !{String wordTextColor = result.unitId() == entry.getIndex() ?
            "text-neutral" :
            switch(unit.translation().familiarity()){
                case UNKNOWN, RECOGNIZED, FAMILIAR -> "text-primary";
                default -> "text-neutral";
            }
        ;}
        !{String wordGapColor = result.unitId() == entry.getIndex() ?
            "border-tertiary" :
            "border-transparent"
        ;}
        !{String phraseGapColor = "border-transparent";}
        @if(unit instanceof SelectedUnit selectedUnit)
            @if(selectedUnit instanceof SelectedWordUnit selectedWordUnit)
                @template.document.word(
                wordId = selectedWordUnit.id(),
                bgColor = wordBgColor,
                filler = wordFiller,
                gapColor = wordGapColor,
                textColor = wordTextColor,
                isPersisted = false,
                documentId = result.documentContentData().documentId(),
                rawText = selectedWordUnit.rawText(),
                translation = selectedWordUnit.translation(),
                currentPage = result.paginationData().currentPageNumber())
            @endif
        @elseif(unit instanceof PhraseUnit phraseUnit)
            !{String gapColor = switch(phraseUnit.translation().familiarity()){
                case UNKNOWN, RECOGNIZED, FAMILIAR -> "text-primary";
                default -> "text-neutral";
            };}
            !{String fillerColor = switch(phraseUnit.translation().familiarity()){
                case UNKNOWN -> "bg-accent";
                case RECOGNIZED -> "bg-accent/80";
                case FAMILIAR -> "bg-accent/60";
                default -> "bg-primary";
            };}
            !{String textColor = switch(phraseUnit.translation().familiarity()){
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
            gapColor = gapColor,
            fillerColor = fillerColor,
            textColor = textColor)
        @elseif(unit instanceof TranslationWordUnit)
            @template.document.word(
            wordId = unit.id(),
            bgColor = wordBgColor,
            filler = wordFiller,
            gapColor = wordGapColor,
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
