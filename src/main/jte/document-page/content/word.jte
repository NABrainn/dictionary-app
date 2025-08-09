@import lule.dictionary.enumeration.Familiarity

@param int wordId
@param int documentId
@param String word
@param String processedWord
@param String bgColor = ""
@param String textColor = ""
@param Familiarity familiarity = null
@param boolean isSelectable = true
@param boolean isPhraseWord = false
@param String pointerEvents = ""


!{var phraseWord = "";}
@if(isPhraseWord)
    !{phraseWord = "phrase-word";}
@endif
@if(familiarity == null)
    !{familiarity = Familiarity.UNKNOWN;}
@endif

@if(isSelectable)
    <div
    class="
    inline-block relative cursor-pointer rounded whitespace-nowrap text-lg word selectable word-${wordId} value-${processedWord}"
    id="selectable-${wordId}"
    data-id="${wordId}"
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
        <span
        class="${familiarity.name().toLowerCase()} selectable-${processedWord} ${bgColor} ${textColor} rounded selectable"
        id="word-span-${wordId}">${word}</span>
    </div>
@else
    <div
    class="
    inline-block relative rounded whitespace-nowrap text-lg pointer-events-none word word-${wordId}"
    id="word-${wordId}"
    data-id="${wordId}"
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
        <span
        class="${familiarity.name().toLowerCase()} ${bgColor} ${textColor} rounded ${phraseWord}"
        >${word}</span>
    </div>
@endif
