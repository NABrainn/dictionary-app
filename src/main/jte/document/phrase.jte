@import gg.jte.support.ForSupport
@import lule.dictionary.translations.data.Translation

@param int documentId
@param int startId
@param int endId
@param int currentPage
@param String phraseText
@param Translation translation
@param String phraseBgColor
@param String phraseTextColor
@param boolean selectable
!{String pointerEvents = !selectable ? "pointer-events-none" : "";}
<div
id="phrase-${startId}"
class="${phraseBgColor} ${phraseTextColor} ${pointerEvents}
flex flex-wrap border gap-1 border-x-2 border-secondary rounded-md cursor-pointer select-none
${translation.familiarity().name().toLowerCase()}"
data-text="${translation.processedTargetWord()}"

_="on click send reloadDocumentWithSelectedPhrase"
hx-trigger="reloadDocumentWithSelectedPhrase once"
hx-get="/documents/${documentId}"
hx-swap="outerHTML"
hx-target="#units"
hx-params="startId,endId,text,isPersisted,page,selection"
hx-vals='{
    "startId": "${startId}",
    "endId": "${endId}",
    "text": "${translation.processedTargetWord()}",
    "isPersisted": "true",
    "page": "${currentPage}",
    "selection": "phrase"
}'>
@for(var entry : ForSupport.of(phraseText.split(" ")))
    <div
    class="
    relative whitespace-nowrap text-lg flex items-center
    phrase-part">
        <div
        class="cursor-default z-2 absolute inline pointer-events-auto"
        id="form-container-${startId}"
        hx-on::after-swap="formPositioner.handleOverflow(htmx.find('#form-container-${startId}'), 3)">
        </div>
    <span
    class="
    phrase-part">${entry.get()}</span>
    </div>
@endfor
</div>
