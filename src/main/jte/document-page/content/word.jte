@import lule.dictionary.enumeration.Familiarity
@import lule.dictionary.service.translation.dto.attribute.TranslationAttribute

@param int wordIndex
@param int importId
@param int page
@param String word
@param String processedWord
@param String bgColor
@param String textColor
@param Familiarity familiarity 
@param TranslationAttribute translationAttribute
@param boolean found = false
@param boolean selected = false
@param boolean selectable = true

@if(selectable)
    <div
    class="
    inline-block relative cursor-pointer rounded whitespace-nowrap text-lg"
    id="target-word-${wordIndex}"
    hx-get="/translations?targetWord=${processedWord}&importId=${importId}&page=${page}"
    hx-target="#translation-form-container-${wordIndex}"
    hx-swap="innerHTML"
    hx-vals='{"selectedWordId": "${wordIndex}"}'
    hx-trigger="click"
    hx-on::before-request="htmx.find('word-highlighter').updateWordId(${wordIndex})">
        <div
        onclick="event.stopPropagation()"
        class="
        cursor-default z-2 absolute inline left-2 top-10"
        id="translation-form-container-${wordIndex}"
        hx-on::after-swap="htmx.find('import-page').updatePositions(${wordIndex})">
        </div>
        @if(familiarity != null)
            <span
            class="${familiarity.name().toLowerCase()} word-${processedWord} ${bgColor} ${textColor} target-word rounded"
            id="target-word-raw-${wordIndex}">${word}</span>
        @else
            <span
            class="unknown word-${processedWord} ${bgColor} ${textColor} target-word rounded"
            id="target-word-raw-${wordIndex}">${word}</span>
        @endif

    </div>
@else
    @if(familiarity != null)
        <span
        class="${bgColor} ${familiarity.name().toLowerCase()} relative ${textColor} px-0.1 py-0.1 rounded whitespace-nowrap"
        id="target-word-${wordIndex}">
        ${word}<div class="cursor-default z-2 inline absolute" id="translation-form-${wordIndex}">
        </div></span>
    @else
        <span
        class="${bgColor} relative ${textColor} px-0.1 py-0.1 rounded whitespace-nowrap"
        id="target-word-${wordIndex}">
        ${word}<div class="cursor-default z-2 inline absolute" id="translation-form-${wordIndex}">
        </div></span>
    @endif
@endif

