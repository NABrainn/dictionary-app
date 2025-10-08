@import lule.dictionary.documents.data.documentProcessing.*
@import lule.dictionary.documents.data.documentProcessing.NewWordUnit
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
hx-target="#form-container-${response.selectedWordInfo().id()}"
hx-swap="innerHTML"
hx-params="documentId,targetWord,id,isPersisted"
hx-vals='{
    "documentId": "${response.contentData().documentId()}",
    "targetWord": "${response.selectedWordInfo().translation().processedTargetWord()}",
    "id": "${response.selectedWordInfo().id()}",
    "isPersisted": "${response.selectedWordInfo().isPersisted()}"
}'>
    @for(Paragraph paragraph : response.contentData().content())
        <div class="flex flex-wrap flex-row">
            @for(var unit : paragraph.units())
                !{String phraseGapColor = "border-transparent";}
                @if(unit instanceof SelectedUnit selectedUnit)
                    @if(selectedUnit instanceof SelectedWordUnit selectedWordUnit)
                        @template.document.word(
                        wordId = selectedWordUnit.id(),
                        bgColor = "bg-tertiary",
                        filler = "bg-tertiary",
                        gapColor = "border-tertiary",
                        textColor = "text-neutral",
                        documentId = response.contentData().documentId(),
                        rawText = selectedWordUnit.rawText(),
                        translation = selectedWordUnit.translation(),
                        currentPage = response.paginationData().currentPage(),
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
                    isPersisted = true,
                    documentId = response.contentData().documentId(),
                    currentPage = response.paginationData().currentPage(),
                    phraseText = unit.rawText(),
                    translation = phraseUnit.translation(),
                    phraseBgColor = phraseBgColor,
                    phraseTextColor = phraseTextColor,
                    selectable = true)
                @elseif(unit instanceof WordUnit wordUnit)
                    !{String wordBgColor = unit.translation() != null ?
                                switch(unit.translation().familiarity()){
                                    case UNKNOWN -> "bg-accent";
                                    case RECOGNIZED -> "bg-accent/80";
                                    case FAMILIAR -> "bg-accent/60";
                                    default -> "bg-primary";
                                } :
                                "bg-primary";
                            ;}
                    !{String wordFiller = "bg-primary";}
                    !{String wordTextColor = unit.translation() != null ?
                                switch(unit.translation().familiarity()){
                                    case UNKNOWN, RECOGNIZED, FAMILIAR -> "text-primary";
                                    default -> "text-neutral";
                                } :
                                "text-neutral";
                    ;}
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
