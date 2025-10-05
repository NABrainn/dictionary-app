@import lule.dictionary.documents.data.documentProcessing.*
@import lule.dictionary.documents.data.response.ReloadWithPhraseResponse
@import lule.dictionary.documents.data.documentProcessing.PhraseUnit
@import lule.dictionary.documents.data.documentProcessing.NewWordUnit
@import lule.dictionary.documents.data.documentProcessing.SelectedUnit
@import lule.dictionary.translations.data.entity.PersistedPhraseTranslation

@param ReloadWithPhraseResponse response
<div
id="units"
class="flex flex-col"
_="on load send openForm to #units"
hx-trigger="openForm once"
hx-get="/translations/"
hx-target="#form-container-${response.selectedPhraseInfo().id()}"
hx-swap="innerHTML"
hx-params="documentId,targetWord,isPhrase,id,isPersisted"
hx-vals='{
    "documentId": "${response.contentData().documentId()}",
    "targetWord": "${response.selectedPhraseInfo().translation().processedTargetWord()}",
    "isPhrase": "true",
    "id": "${response.selectedPhraseInfo().id()}",
    "isPersisted": "${response.selectedPhraseInfo().isPersisted()}"
}'>
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
            @if(unit instanceof SelectedUnit selectedUnit)
                @if(selectedUnit instanceof SelectedPhraseUnit selectedPhraseUnit)
                    !{String gapColor = "bg-tertiary";}
                    !{String fillerColor = "bg-tertiary";}
                    !{String textColor = "text-neutral";}
                    @template.document.phrase(
                    startId = response.selectedPhraseInfo().id(),
                    isPersisted = response.selectedPhraseInfo().isPersisted(),
                    documentId = response.contentData().documentId(),
                    currentPage = response.paginationData().currentPageNumber(),
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
                @if(response.selectedPhraseInfo().translation().processedTargetWord().equals(unit.translation().processedTargetWord()) && !(unit.translation() instanceof PersistedPhraseTranslation))
                    @template.document.phrase(
                    startId = unit.id(),
                    isPersisted = true,
                    documentId = response.contentData().documentId(),
                    currentPage = response.paginationData().currentPageNumber(),
                    phraseText = unit.rawText(),
                    translation = phraseUnit.translation(),
                    phraseBgColor = phraseBgColor,
                    phraseTextColor = phraseTextColor,
                    selectable = false)
                @else
                    @template.document.phrase(
                    startId = unit.id(),
                    isPersisted = true,
                    documentId = response.contentData().documentId(),
                    currentPage = response.paginationData().currentPageNumber(),
                    phraseText = unit.rawText(),
                    translation = phraseUnit.translation(),
                    phraseBgColor = phraseBgColor,
                    phraseTextColor = phraseTextColor,
                    selectable = true)
                @endif
            @elseif(unit instanceof PersistedWordUnit)
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
            @elseif(unit instanceof NewWordUnit)
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
