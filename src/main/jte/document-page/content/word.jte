@import lule.dictionary.enumeration.Familiarity

@param int wordId
@param int documentId
@param String word
@param String processedWord
@param String bgColor = ""
@param String textColor = ""
@param Familiarity familiarity = null
@param boolean selectable = true
@param String pointerEvents = "pointer-events-auto"


@if(selectable)
    <div
    class="
    inline-block relative cursor-pointer rounded whitespace-nowrap text-lg selectable"
    id="target-word-${wordId}"
    hx-get="/translations?targetWord=${processedWord}&documentId=${documentId}"
    hx-target="#translation-form-container-${wordId}"
    hx-swap="innerHTML"
    hx-vals='{"selectedWordId": "${wordId}"}'
    hx-trigger="click"
    hx-on::before-request="htmx.find('selectable-highlighter').updateSelectableId(${wordId})">
        <div
        onclick="event.stopPropagation()"
        class="
        cursor-default z-2 absolute inline left-2 top-10"
        id="translation-form-container-${wordId}"
        hx-on::after-swap="htmx.find('import-page').updatePositions(${wordId})">
        </div>
        @if(familiarity != null)
            <span
            class="${familiarity.name().toLowerCase()} selectable-${processedWord} ${bgColor} ${textColor} target-word rounded selectable"
            data-selectable-value="${processedWord}"
            id="selectable-${wordId}">${word}</span>
        @else
            <span
            class="unknown word-${processedWord} ${bgColor} ${textColor} target-word rounded selectable"
            data-selectable-value="${processedWord}"
            id="selectable-${wordId}">${word}</span>
        @endif
    </div>
@else
    <div
    class="
    inline-block relative cursor-pointer rounded whitespace-nowrap text-lg pointer-events-none"
    id="target-word-${wordId}"
    hx-get="/translations?targetWord=${processedWord}&documentId=${documentId}"
    hx-target="#translation-form-container-${wordId}"
    hx-swap="innerHTML"
    hx-vals='{"selectedWordId": "${wordId}"}'
    hx-trigger="click"
    hx-on::before-request="htmx.find('selectable-highlighter').updateSelectableId(${wordId})">
        <div
        onclick="event.stopPropagation()"
        class="
        cursor-default z-2 absolute inline left-2 top-10"
        id="translation-form-container-${wordId}"
        hx-on::after-swap="htmx.find('import-page').updatePositions(${wordId})">
        </div>
        @if(familiarity != null)
            <span
            class="${familiarity.name().toLowerCase()} word-${processedWord} ${bgColor} ${textColor} target-word rounded"
            id="selectable-${wordId}">${word}</span>
        @else
            <span
            class="unknown word-${processedWord} ${bgColor} ${textColor} target-word rounded"
            id="selectable-${wordId}">${word}</span>
        @endif
    </div>
@endif

