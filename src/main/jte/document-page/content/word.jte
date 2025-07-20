@import lule.dictionary.service.translation.dto.attribute.TranslationAttribute

@param int wordIndex
@param int importId
@param int page
@param String word
@param String processedWord
@param String bgColor
@param String textColor
@param TranslationAttribute translationAttribute
@param boolean found = false
@param boolean selected = false
@param boolean selectable = true

@if(selectable)
    <div
    class="
    word-${processedWord} ${bgColor} ${textColor} inline relative cursor-pointer rounded whitespace-nowrap text-lg"
    id="target-word-${wordIndex}"
    hx-get="/translations?targetWord=${processedWord}&importId=${importId}&page=${page}"
    hx-target="#translation-form-container-${wordIndex}"
    hx-swap="innerHTML"
    hx-vals='{"selectedWordId": "${wordIndex}"}'
    hx-trigger="click"
    hx-on::before-request="htmx.find('import-page').updateWordId(${wordIndex})">
        <div
        onclick="event.stopPropagation()"
        class="
        cursor-default z-2 absolute inline left-2 top-10"
        id="translation-form-container-${wordIndex}"
        hx-on::after-swap="htmx.find('import-page').updatePositions(${wordIndex})">
        </div><span id="target-word-raw-${wordIndex}">${word}</span></div>
@else
    <span
    class="${bgColor} relative ${textColor} px-0.1 py-0.1 rounded whitespace-nowrap"
    id="target-word-${wordIndex}">
    ${word}<div class="cursor-default z-2 inline absolute" id="translation-form-${wordIndex}">
    </div></span>
@endif

