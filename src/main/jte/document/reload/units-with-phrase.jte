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
            @if(unit instanceof SelectedUnit selectedUnit)
                @if(selectedUnit instanceof SelectedPhraseUnit selectedPhraseUnit)
                    !{String gapColor = "bg-tertiary";}
                    !{String fillerColor = "bg-tertiary";}
                    !{String textColor = "text-neutral";}
                    @template.document.phrase(
                    startId = response.selectedPhraseInfo().id(),
                    isPersisted = response.selectedPhraseInfo().isPersisted(),
                    documentId = response.contentData().documentId(),
                    currentPage = response.paginationData().currentPage(),
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
                    currentPage = response.paginationData().currentPage(),
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
                    currentPage = response.paginationData().currentPage(),
                    phraseText = unit.rawText(),
                    translation = phraseUnit.translation(),
                    phraseBgColor = phraseBgColor,
                    phraseTextColor = phraseTextColor,
                    selectable = true)
                @endif
            @elseif(unit instanceof WordUnit wordUnit)
                !{String wordBgColor = unit.translation() != null ?
                    switch(unit.translation().familiarity()){
                        case UNKNOWN -> "bg-accent";
                        case RECOGNIZED -> "bg-accent/80";
                        case FAMILIAR -> "bg-accent/60";
                        default -> "bg-primary";
                    } :
                    "bg-primary";
                }
                !{String wordFiller = "bg-primary";}
                !{String wordTextColor = unit.translation() != null ?
                    switch(unit.translation().familiarity()){
                        case UNKNOWN, RECOGNIZED, FAMILIAR -> "text-primary";
                        default -> "text-neutral";
                    } :
                    "text-neutral";
                }
                !{String wordGapColor = "border-transparent";}
                @if(wordUnit instanceof PersistedWordUnit)
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
                @elseif(wordUnit instanceof NewWordUnit newWordUnit)
                    @if(unit.translation().processedTargetWord().length() <= 50)
                        @template.document.word(
                        wordId = newWordUnit.id(),
                        bgColor = wordBgColor,
                        gapColor = wordGapColor,
                        filler = wordFiller,
                        textColor = wordTextColor,
                        isPersisted = false,
                        documentId = response.contentData().documentId(),
                        rawText = newWordUnit.rawText(),
                        translation = newWordUnit.translation(),
                        currentPage = response.paginationData().currentPage())
                    @endif
                @elseif(wordUnit instanceof InvalidWordUnit invalidWordUnit)
                    @template.document.word(
                    wordId = invalidWordUnit.id(),
                    bgColor = "bg-primary",
                    gapColor = wordGapColor,
                    filler = wordFiller,
                    textColor = "text-neutral",
                    isPersisted = false,
                    documentId = response.contentData().documentId(),
                    rawText = invalidWordUnit.rawText(),
                    translation = invalidWordUnit.translation(),
                    currentPage = response.paginationData().currentPage())
                @endif
            @endif
            @endfor
        </div>
        !{int endlineSize = paragraph.endlineSize();}
        @for(int i = 0; i < endlineSize / 20; i++)
            <div class="invisible">placeholder</div>
        @endfor
    @endfor
</div>
