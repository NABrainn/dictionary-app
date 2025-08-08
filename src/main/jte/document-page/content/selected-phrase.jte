@import gg.jte.support.ForSupport
@import lule.dictionary.enumeration.Familiarity
@import lule.dictionary.service.imports.importService.dto.Phrase
@import java.util.List

@param Phrase phrase 
@param String targetWords
@param List<String> familiarities
@param int selectableId
@param int documentId

!{var containerBgColor = "bg-primary";}
!{var containerTextColor = "text-neutral";}

!{var familiarityEnums = familiarities.stream().map(familiarity -> switch (familiarity) {
        case "recognized" -> Familiarity.RECOGNIZED;
        case "familiar" -> Familiarity.FAMILIAR;
        case "known" -> Familiarity.KNOWN;
        case "ignored" -> Familiarity.IGNORED;
        default -> Familiarity.UNKNOWN;
    })
    .toList();
}

<div
class="inline-flex relative rounded text-lg gap-1 cursor-pointer border border-3 border-secondary selected-phrase selectable selectable-${targetWords.replaceAll("\\p{P}", "").replace(" ", "-").toLowerCase()}"
id="selectable-${selectableId}"
data-selectable-value="${targetWords.replace(" ", "-")}">
    <div
    onclick="event.stopPropagation()"
    class="
    cursor-default z-2 absolute inline left-2 top-10 select-phrase-form-container"
    id="translation-form-container-${selectableId}"
    hx-on::after-swap="htmx.find('import-page').updatePositions(${selectableId})"
    hx-get="/translations/"
    hx-target="this"
    hx-swap="innerHTML"
    hx-vals='{
        "documentId": "${documentId}",
        "targetWord": "${targetWords.toLowerCase()}",
        "selectedWordId": "${selectableId}",
        "isPhrase": "true"
    }'
    hx-trigger="revealed">
    </div>
    @for(var wordEntry : ForSupport.of(targetWords.split(" ")))
        @template.document-page.content.word(
            wordId = selectableId + wordEntry.getIndex(),
            bgColor = containerBgColor,
            textColor = containerTextColor,
            processedWord = wordEntry.get(),
            documentId = documentId,
            word = wordEntry.get(),
            familiarity = familiarityEnums.get(wordEntry.getIndex()),
            selectable = false,
            isPhraseWord = true
        )
    @endfor
</div>
