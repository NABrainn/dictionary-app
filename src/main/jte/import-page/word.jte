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

<span
class="${bgColor} relative ${textColor} px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap"
id="target-word-${wordIndex}"
hx-get="/translations?targetWord=${processedWord}&importId=${importId}&page=${page}"
hx-target="#translation-form-${wordIndex}"
hx-swap="innerHTML"
hx-vals='{"selectedWordId": "${wordIndex}"}'
hx-on::before-request="document.querySelector('import-page').onWordClick(${wordIndex})">
${word}<div class="cursor-default z-2 inline absolute top-8" id="translation-form-${wordIndex}" onclick="event.stopPropagation();">

        @if(translationAttribute != null)
                @template.import-page.translation.update-translation-form(translationAttribute = translationAttribute)
        @endif
    </div></span>