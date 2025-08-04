@import lule.dictionary.enumeration.Familiarity

@param int wordId
@param int documentId
@param String word
@param String processedWord
@param String bgColor
@param String textColor
@param Familiarity familiarity = null
@param boolean selectable = true
@param String pointerEvents = null
@param String phraseWord = null

@if(selectable)
        <div
        class="
        inline-block relative cursor-pointer rounded whitespace-nowrap text-lg ${pointerEvents}"
        id="target-word-${wordId}"
        hx-get="/translations?targetWord=${processedWord}&documentId=${documentId}"
        hx-target="#translation-form-container-${wordId}"
        hx-swap="innerHTML"
        hx-vals='{"selectedWordId": "${wordId}"}'
        hx-trigger="click"
        hx-on::before-request="htmx.find('selectable-highlighter').updateWordId(${wordId})">

        <div
        onclick="event.stopPropagation()"
        class="
        cursor-default z-2 absolute inline left-2 top-10"
        id="translation-form-container-${wordId}"
        hx-on::after-swap="htmx.find('import-page').updatePositions(${wordId})">
        </div>
        @if(familiarity != null)
            <span
            class="${familiarity.name().toLowerCase()} word-${processedWord} ${bgColor} ${textColor} target-word rounded ${phraseWord}"
            id="target-word-raw-${wordId}">${word}</span>
        @else
            <span
            class="unknown word-${processedWord} ${bgColor} ${textColor} target-word rounded ${phraseWord}"
            id="target-word-raw-${wordId}">${word}</span>
        @endif

    </div>
@else
    @if(familiarity != null)
        <span
        class="${bgColor} ${familiarity.name().toLowerCase()} relative ${textColor} px-0.1 py-0.1 rounded whitespace-nowrap"
        id="target-word-${wordId}">
        ${word}<div class="cursor-default z-2 inline absolute" id="translation-form-${wordId}">
        </div></span>
    @else
        <span
        class="${bgColor} relative ${textColor} px-0.1 py-0.1 rounded whitespace-nowrap"
        id="target-word-${wordId}">
        ${word}<div class="cursor-default z-2 inline absolute" id="translation-form-${wordId}">
        </div></span>
    @endif
@endif

