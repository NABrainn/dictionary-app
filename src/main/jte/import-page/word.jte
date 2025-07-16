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
    @if(translationAttribute != null)
        @if(translationAttribute.formPositionData() != null)
            <span
            class="${bgColor} ${textColor} inline-block relative px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap text-lg"
            id="target-word-${wordIndex}"
            hx-get="/translations?targetWord=${processedWord}&importId=${importId}&page=${page}"
            hx-target="#translation-form-container-${wordIndex}"
            hx-swap="innerHTML"
            hx-vals='{"selectedWordId": "${wordIndex}"}'
            hx-trigger="click"
            hx-on::before-request="document.querySelector('import-page').onWordClickBefore(${wordIndex})">
                <div
                onclick="event.preventDefault()"
                class="
                cursor-default z-2
                ${translationAttribute.formPositionData().left()}
                ${translationAttribute.formPositionData().right()}
                ${translationAttribute.formPositionData().top()}
                ${translationAttribute.formPositionData().bottom()} absolute inline-block"
                id="translation-form-container-${wordIndex}"
                hx-on::after-swap="document.querySelector('import-page').updatePositions(${wordIndex})">
                    @template.import-page.translation.update-translation-form(translationAttribute = translationAttribute)
                </div>${word}</span>
        @endif
    @else
        <span
        class="${bgColor} ${textColor} inline-block relative px-0.1 py-0.1 cursor-pointer rounded whitespace-nowrap text-lg"
        id="target-word-${wordIndex}"
        hx-get="/translations?targetWord=${processedWord}&importId=${importId}&page=${page}"
        hx-target="#translation-form-container-${wordIndex}"
        hx-swap="innerHTML"
        hx-vals='{"selectedWordId": "${wordIndex}"}'
        hx-trigger="click"
        hx-on::before-request="document.querySelector('import-page').onWordClickBefore(${wordIndex})">
            <div
            onclick="event.stopPropagation()"
            class="
            cursor-default z-2 absolute inline-block left-2 top-10"
            id="translation-form-container-${wordIndex}"
            hx-on::after-swap="document.querySelector('import-page').updatePositions(${wordIndex})">
            </div>${word}</span>
    @endif
@else
    <span
    class="${bgColor} relative ${textColor} px-0.1 py-0.1 rounded whitespace-nowrap"
    id="target-word-${wordIndex}">
    ${word}<div class="cursor-default z-2 inline absolute" id="translation-form-${wordIndex}">
    </div></span>
@endif

