@import lule.dictionary.documents.data.documentProcessing.*
@import lule.dictionary.documents.data.documentProcessing.NonTranslationWordUnit
@import lule.dictionary.documents.data.documentProcessing.PhraseUnit
@import gg.jte.support.ForSupport
@import lule.dictionary.documents.data.result.ReloadWithWordResult

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
        !{String wordBgColor = entry.getIndex() == result.unitId() ?
            "bg-tertiary" :
            switch(unit.translation().familiarity()){
                case UNKNOWN -> "bg-accent";
                case RECOGNIZED -> "bg-accent/80";
                case FAMILIAR -> "bg-accent/60";
                default -> "bg-primary";
            }
        ;}
        !{String wordTextColor = entry.getIndex() == result.unitId() ?
            "text-neutral" :
            switch(unit.translation().familiarity()){
                case UNKNOWN, RECOGNIZED, FAMILIAR -> "text-primary";
                default -> "text-neutral";
            }
        ;}
        @if(unit instanceof PhraseUnit)
            nt
        @elseif(unit instanceof lule.dictionary.documents.data.documentProcessing.TranslationWordUnit)
            @template.document.word(
            wordId = entry.getIndex(),
            bgColor = wordBgColor,
            filler = "bg-primary",
            textColor = wordTextColor,
            isPersisted = true,
            documentId = result.documentContentData().documentId(),
            rawText = unit.rawText(),
            translation = unit.translation(),
            currentPage = result.paginationData().currentPageNumber(),
            gapColor = "border-transparent")
        @elseif(unit instanceof NonTranslationWordUnit)
            @if(unit.translation().processedTargetWord().length() <= 50)
                @template.document.word(
                wordId = entry.getIndex(),
                bgColor = wordBgColor,
                filler = "bg-primary",
                textColor = wordTextColor,
                isPersisted = false,
                documentId = result.documentContentData().documentId(),
                rawText = unit.rawText(),
                translation = unit.translation(),
                currentPage = result.paginationData().currentPageNumber(),
                gapColor = "border-transparent")
            @endif
        @endif
    @endfor
</div>
