@import lule.dictionary.translations.data.Translation

@param int wordId
@param String bgColor = ""
@param String textColor = ""
@param boolean isSelectable = true
@param boolean isPhraseWord = false
@param boolean isPersisted = false
@param boolean isWrapped = false

@param int documentId
@param int currentPage
@param String rawText
@param Translation translation
<input
id="word-document-reloader-${wordId}"
hx-trigger="reload-document once"
hx-get="/documents/${documentId}"
hx-swap="outerHTML"
hx-target="#content-main"
hx-params="selectedId,selectedTargetWord,isSelectedPersisted,type,page"
hx-vals='{
    "selectedId": "${wordId}",
    "selectedTargetWord": "${rawText}",
    "isSelectedPersisted": "${isPersisted}",
    "type": "reload",
    "page": "${currentPage}"
}'
type="hidden">
@if(isSelectable)
    <div
    class="
    ${isWrapped ? "pointer-events-none" : "border border-2  border-transparent"} inline-block rounded-none relative cursor-pointer rounded whitespace-nowrap text-lg"
    hx-on:click="htmx.trigger('#word-document-reloader-${wordId}', 'reload-document')">
        <div
        class="cursor-default z-2 absolute inline"
        id="form-container-${wordId}"
        hx-on::after-swap="
        formPositioner.handleOverflow(htmx.find('#form-container-${wordId}'), 3)">
        </div>
        <span
        class="${bgColor} ${textColor} rounded">${rawText}</span>
    </div>
@else
    <div
    class="
    inline-block relative rounded whitespace-nowrap text-lg pointer-events-none select-none">
        <span class="${bgColor} ${textColor} rounded">${rawText}</span>
    </div>
@endif