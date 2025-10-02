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
@param String reloader
<input
id="${reloader}"
hx-trigger="reloadDocument once"
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
}'
type="hidden">

<div
class="${phraseBgColor} ${phraseTextColor}
flex flex-wrap border gap-1 border-x-2 border-secondary rounded-md cursor-pointer select-none
${translation.familiarity().name().toLowerCase()}"
data-text="${translation.processedTargetWord()}"
_="on click send reloadDocument to #${reloader}"
id="phrase-${phraseId}">
@for(var entry : ForSupport.of(phraseText.split(" ")))
    <div
    class="
    relative whitespace-nowrap text-lg flex items-center
    phrase-part">
        <div
        class="cursor-default z-2 absolute inline"
        id="form-container-${phraseId}"
        hx-on::after-swap="formPositioner.handleOverflow(htmx.find('#form-container-${phraseId}'), 3)">
        </div>
    <span
    class="
    phrase-part">${entry.get()}</span>
    </div>
@endfor
</div>
