@import lule.dictionary.documents.data.result.ReloadWithPhraseResult
@import lule.dictionary.documents.data.documentProcessing.DocumentUnit
@import lule.dictionary.documents.data.documentProcessing.PhraseUnit
@import lule.dictionary.documents.data.documentProcessing.TranslationUnit
@import lule.dictionary.documents.data.documentProcessing.WordUnit
@import gg.jte.support.ForSupport
@import java.util.List
@import java.util.stream.IntStream

@param ReloadWithPhraseResult result
<div
id="units"
class="flex inline-flex flex-wrap"
_="on load send openForm to #units"
hx-trigger="openForm once"
hx-get="/translations/"
hx-target="#form-container-${result.startId()}"
hx-swap="innerHTML"
hx-params="documentId,targetWord,id,isPersisted"
hx-vals='{
    "documentId": "${result.documentContentData().documentId()}",
    "targetWord": "${result.targetWord()}",
    "id": "${result.startId()}",
    "isPersisted": "${result.isSelectablePersisted()}"
}'>
    !{List<Integer> range = IntStream.range(result.startId(), result.endId() + 1).boxed().toList();}
    @for(var entry :  ForSupport.of(result.documentContentData().content()))
        !{DocumentUnit unit = entry.get();}
        !{String bgColor = range.stream().anyMatch(num -> num == entry.getIndex()) ?
            "bg-tertiary" :
            switch(unit.translation().familiarity()){
                case UNKNOWN -> "bg-accent";
                case RECOGNIZED -> "bg-accent/80";
                case FAMILIAR -> "bg-accent/60";
                default -> "bg-primary";
            }
        ;}
        !{String filler = range.stream().anyMatch(num -> num == entry.getIndex()) ?
            "bg-tertiary" :
            "bg-primary";}
        !{String textColor = range.stream().anyMatch(num -> num == entry.getIndex()) ?
            "text-neutral" :
            switch(unit.translation().familiarity()){
                case UNKNOWN, RECOGNIZED, FAMILIAR -> "text-primary";
                default -> "text-neutral";
            }
        ;}
        !{String gapColor = range.stream().anyMatch(num -> num == entry.getIndex()) ?
            "border-tertiary" :
            "border-transparent";}
        @if(unit instanceof PhraseUnit)
            nt
        @elseif(unit instanceof TranslationUnit)
            @template.document.word(
            wordId = entry.getIndex(),
            bgColor = bgColor,
            filler = filler,
            gapColor = gapColor,
            textColor = textColor,
            isPersisted = true,
            documentId = result.documentContentData().documentId(),
            rawText = unit.rawText(),
            translation = unit.translation(),
            currentPage = result.paginationData().currentPageNumber())
        @elseif(unit instanceof WordUnit)
            @if(unit.translation().processedTargetWord().length() <= 50)
                @template.document.word(
                wordId = entry.getIndex(),
                bgColor = bgColor,
                filler = filler,
                gapColor = gapColor,
                textColor = textColor,
                isPersisted = false,
                documentId = result.documentContentData().documentId(),
                rawText = unit.rawText(),
                translation = unit.translation(),
                currentPage = result.paginationData().currentPageNumber())
            @else
                @template.document.word(
                wordId = entry.getIndex(),
                bgColor = "bg-primary",
                filler = filler,
                gapColor = gapColor,
                textColor = "text-neutral",
                isSelectable = false,
                isPersisted = false,
                documentId = result.documentContentData().documentId(),
                rawText = unit.rawText(),
                translation = unit.translation(),
                currentPage = result.paginationData().currentPageNumber())
            @endif
        @endif
    @endfor
</div>
