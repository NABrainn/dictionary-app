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
    <div class="relative inline">
        <span
        class="${bgColor} ${textColor} px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap text-lg"
        id="target-word-${wordIndex}"
        hx-get="/translations?targetWord=${processedWord}&importId=${importId}&page=${page}"
        hx-target="#translation-form-${wordIndex}"
        hx-swap="innerHTML"
        hx-vals='{"selectedWordId": "${wordIndex}"}'
        hx-on::before-request="document.querySelector('import-page').onWordClickBefore(${wordIndex})"
        hx-on::after-request="document.querySelector('import-page').onWordClickAfter(${wordIndex})">
        ${word}</span>
        <div
        class="
        inline cursor-default z-2 top-8  absolute inline" id="translation-form-${wordIndex}" onclick="event.stopPropagation();">
            @if(translationAttribute != null)
                @template.import-page.translation.update-translation-form(translationAttribute = translationAttribute)
            @endif
        </div>
    </div>
@else
    <span
    class="${bgColor} relative ${textColor} px-0.1 py-0.1 rounded whitespace-nowrap"
    id="target-word-${wordIndex}">
    ${word}<div class="cursor-default z-2 inline absolute" id="translation-form-${wordIndex}" onclick="event.stopPropagation();">
    </div></span>
@endif
<translation-form-aligner></translation-form-aligner>
