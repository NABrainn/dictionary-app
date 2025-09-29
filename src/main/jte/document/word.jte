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
@param String gapColor
@param String filler

@if(isSelectable)
    <input
    id="word-document-reloader-${wordId}"
    hx-trigger="reload-document once"
    hx-get="/documents/${documentId}/withSelectedWord"
    hx-swap="outerHTML"
    hx-target="#units"
    hx-params="unitId,unitText,isUnitPersisted,page"
    hx-vals='{
        "unitId": "${wordId}",
        "unitText": "${rawText}",
        "isUnitPersisted": "${isPersisted}",
        "page": "${currentPage}"
    }'
    type="hidden">
    <div
    class="
    border-2 ${gapColor} ${filler} rounded-none relative cursor-pointer rounded whitespace-nowrap text-lg"
    hx-on:click="htmx.trigger('#word-document-reloader-${wordId}', 'reload-document')">
        <div
        class="cursor-default z-2 absolute inline"
        id="form-container-${wordId}"
        hx-on::after-swap="
        formPositioner.handleOverflow(htmx.find('#form-container-${wordId}'), 3)">
        </div>
        <span
        id="word-${wordId}"
        data-text="${translation.processedTargetWord()}"
        class="
        ${bgColor} ${textColor} rounded
        ${translation.familiarity().name().toLowerCase()}">${rawText}</span>
    </div>
@else
    <div
    class="
    inline-block relative rounded whitespace-nowrap text-lg pointer-events-none">
        <span
        id="word-${wordId}"
        class="${bgColor} ${textColor} rounded select-none">${rawText}</span>
    </div>
@endif