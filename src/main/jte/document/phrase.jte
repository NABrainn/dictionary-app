@import gg.jte.support.ForSupport
@import lule.dictionary.translations.data.Translation

@param int phraseLength
@param int phraseId
@param int documentId
@param int currentPage
@param String phraseText
@param Translation translation
@param String phraseBgColor
@param String phraseTextColor
@param boolean selectable
!{String pointerEvents = !selectable ? "pointer-events-none" : "";}
<div
id="phrase-${phraseId}"
class="${phraseBgColor} ${phraseTextColor} ${pointerEvents}
flex flex-wrap border gap-1 border-x-2 border-secondary rounded-md cursor-pointer select-none
${translation.familiarity().name().toLowerCase()}"
data-text="${translation.processedTargetWord()}"

_="on click send reloadDocumentWithSelectedPhrase"
hx-trigger="reloadDocumentWithSelectedPhrase once"
hx-get="/documents/${documentId}/withSelectedPhrase"
hx-swap="outerHTML"
hx-target="#units"
hx-params="startId,length,unitText,isUnitPersisted,page"
hx-vals='{
    "startId": "${phraseId}",
    "length": "${translation.processedTargetWord().split(" ").length}",
    "unitText": "${phraseText}",
    "isUnitPersisted": "true",
    "page": "${currentPage}"
}'>
@for(var entry : ForSupport.of(phraseText.split(" ")))
    <div
    class="
    relative whitespace-nowrap text-lg flex items-center
    phrase-part">
        <div
        class="cursor-default z-2 absolute inline pointer-events-auto"
        id="form-container-${phraseId}"
        hx-on::after-swap="formPositioner.handleOverflow(htmx.find('#form-container-${phraseId}'), 3)">
        </div>
    <span
    class="
    phrase-part">${entry.get()}</span>
    </div>
@endfor
</div>
