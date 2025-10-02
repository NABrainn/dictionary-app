@import gg.jte.support.ForSupport
@import lule.dictionary.translations.data.Translation

@param int phraseLength
@param int phraseId
@param int documentId
@param int currentPage
@param String phraseText
@param Translation translation
@param String gapColor
@param String fillerColor
@param String textColor
<input
id="word-document-reloader-${phraseId}"
hx-trigger="reload-document once"
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
class="
flex flex-wrap border-2 border-secondary rounded-md
${translation.familiarity().name().toLowerCase()}"
data-text="${translation.processedTargetWord()}"
id="phrase-${phraseId}">
@for(var entry : ForSupport.of(phraseText.split(" ")))
    @if(entry.getIndex() == 0)
        <div
        class="
        ${gapColor} ${fillerColor}
        border-x-2 border-transparent rounded-tl rounded-bl rounded-tr-none rounded-br-none
        relative cursor-pointer rounded whitespace-nowrap text-lg
        phrase-part"
        hx-on:click="htmx.trigger('#word-document-reloader-${phraseId}', 'reload-document')">
            <div
            class="cursor-default z-2 absolute inline"
            id="form-container-${phraseId}"
            hx-on::after-swap="
            formPositioner.handleOverflow(htmx.find('#form-container-${phraseId}'), 3)">
            </div>
            <span
            class="
            ${textColor} rounded
            phrase-part">${entry.get()}</span>
        </div>
    @elseif(entry.getIndex() == phraseLength - 1)
        <div
        class="
        ${gapColor} ${fillerColor} border-x-2 border-transparent rounded-tr rounded-br rounded-tl-none rounded-bl-none
         relative cursor-pointer rounded whitespace-nowrap text-lg
        phrase-part"
        hx-on:click="htmx.trigger('#word-document-reloader-${phraseId}', 'reload-document')">
            <div
            class="cursor-default z-2 absolute inline"
            id="form-container-${phraseId}"
            hx-on::after-swap="formPositioner.handleOverflow(htmx.find('#form-container-${phraseId}'), 3)">
            </div>
            <span
            class="
            ${textColor} rounded
            phrase-part">${entry.get()}</span>
        </div>
    @else
        <div
        class="
        ${gapColor} ${fillerColor} border-x-2 border-transparent rounded-none relative cursor-pointer rounded whitespace-nowrap text-lg
        phrase-part"
        hx-on:click="htmx.trigger('#word-document-reloader-${phraseId}', 'reload-document')">
            <div
            class="cursor-default z-2 absolute inline"
            id="form-container-${phraseId}"
            hx-on::after-swap="formPositioner.handleOverflow(htmx.find('#form-container-${phraseId}'), 3)">
            </div>
        <span
        class="
        ${textColor}
        phrase-part">${entry.get()}</span>
        </div>
    @endif
@endfor
</div>
