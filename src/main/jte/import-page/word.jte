@param int wordIndex
@param int importId
@param String word
@param String processedWord
@param String bgColor
@param String textColor

<span
class="${bgColor} relative ${textColor} px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
id="target-word-${wordIndex}"
hx-get="/translations?targetWord=${processedWord}&importId=${importId}"
hx-target="#translation-form-${wordIndex}"
hx-swap="innerHTML"
hx-vals='{"selectedWordId": "${wordIndex}"}'
hx-on::before-request="document.querySelector('import-page').onWordClick(${wordIndex})">
${word}<div class="cursor-default z-2 inline absolute left-0 top-16" id="translation-form-${wordIndex}" onclick="event.stopPropagation();"></div></span>