@import lule.dictionary.documents.data.attribute.DocumentReloadAttribute
@import lule.dictionary.documents.data.documentProcessing.DocumentUnit
@import lule.dictionary.documents.data.documentProcessing.PhraseUnit
@import lule.dictionary.documents.data.documentProcessing.TranslationUnit
@import lule.dictionary.documents.data.documentProcessing.WordUnit
@import gg.jte.support.ForSupport

@param DocumentReloadAttribute attribute
<div
hx-trigger="open-form once"
hx-get="/translations/"
hx-target="#form-container-${attribute.selectedId()}"
hx-swap="innerHTML"
hx-params="documentId,targetWord,id,isPersisted"
hx-vals='{
    "documentId": "${attribute.documentContentData().documentId()}",
    "targetWord": "${attribute.targetWord()}",
    "id": "${attribute.selectedId()}",
    "isPersisted": "${attribute.isSelectablePersisted()}"
}'
hx-on::load="htmx.trigger('#content-main', 'open-form');"
id="content-main"
class="flex inline-flex flex-wrap">
    @for(var entry :  ForSupport.of(attribute.documentContentData().content()))
        !{DocumentUnit unit = entry.get();}
        !{String wordBgColor = entry.getIndex() == attribute.selectedId() ?
            "bg-tertiary" :
            switch(unit.translation().familiarity()){
                case UNKNOWN -> "bg-accent";
                case RECOGNIZED -> "bg-accent/80";
                case FAMILIAR -> "bg-accent/60";
                default -> "bg-primary";
            }
        ;}
        !{String wordTextColor = entry.getIndex() == attribute.selectedId() ?
            "text-neutral" :
            switch(unit.translation().familiarity()){
                case UNKNOWN, RECOGNIZED, FAMILIAR -> "text-primary";
                default -> "text-neutral";
            }
        ;}
        @if(unit instanceof PhraseUnit)
            @template.translation.update-phrase(
            translation = unit.translation(),
            selectableId = entry.getIndex())
        @elseif(unit instanceof TranslationUnit)
            @template.document.word(
            wordId = entry.getIndex(),
            bgColor = wordBgColor,
            textColor = wordTextColor,
            isPersisted = true,
            documentId = attribute.documentContentData().documentId(),
            rawText = unit.rawText(),
            translation = unit.translation(),
            currentPage = attribute.paginationData().currentPageNumber())
        @elseif(unit instanceof WordUnit)
            @if(unit.translation().processedTargetWord().length() <= 50)
                @template.document.word(
                wordId = entry.getIndex(),
                bgColor = wordBgColor,
                textColor = wordTextColor,
                isPersisted = false,
                documentId = attribute.documentContentData().documentId(),
                rawText = unit.rawText(),
                translation = unit.translation(),
                currentPage = attribute.paginationData().currentPageNumber())
            @else
                @template.document.word(
                wordId = entry.getIndex(),
                bgColor = "bg-primary",
                textColor = "text-neutral",
                isSelectable = false,
                isPersisted = false,
                documentId = attribute.documentContentData().documentId(),
                rawText = unit.rawText(),
                translation = unit.translation(),
                currentPage = attribute.paginationData().currentPageNumber())
            @endif
        @endif
    @endfor
</div>
