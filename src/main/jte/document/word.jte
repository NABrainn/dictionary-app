@import lule.dictionary.translations.data.entity.EmptyTranslation
@import lule.dictionary.translations.data.entity.Translation

@param int wordId
@param String bgColor = ""
@param String textColor = ""
@param boolean isPersisted

@param int documentId
@param int currentPage
@param String rawText
@param Translation translation
@param String gapColor
@param String filler
<div
class="
border-2 ${gapColor} ${filler} relative ${!(translation instanceof EmptyTranslation) ? "cursor-pointer" :  "cursor-default pointer-events-none"} rounded whitespace-nowrap text-lg"
_="on click send reloadWithSelectedWord"
hx-trigger="reloadWithSelectedWord once"
hx-get="/documents/${documentId}"
hx-swap="outerHTML"
hx-target="#units"
hx-params="startId,isPersisted,page,selection"
hx-vals='{
    "startId": "${wordId}",
    "isPersisted": "${isPersisted}",
    "page": "${currentPage}",
    "selection": "word"
}'>
    <div
    class="cursor-default z-2 absolute inline"
    id="form-container-${wordId}"
    hx-on::after-swap="formPositioner.handleOverflow(htmx.find('#form-container-${wordId}'), 3)">
    </div>
    <span
    id="word-${wordId}"
    data-text="${translation.processedTargetWord()}"
    data-raw="${rawText}"
    data-valid="${!(translation instanceof EmptyTranslation) ? "true" : "false"}"
    class="
    ${bgColor} ${textColor} rounded
    ${translation.familiarity().name().toLowerCase()}">${rawText}</span>
</div>
