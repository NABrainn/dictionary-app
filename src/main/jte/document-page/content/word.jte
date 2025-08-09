@import lule.dictionary.dto.database.interfaces.translation.Translation

@param int wordId
@param int documentId
@param String word
@param String processedWord
@param String bgColor = ""
@param String textColor = ""
@param boolean isSelectable = true
@param boolean isPhraseWord = false
@param Translation translation = null


!{var phraseWord = "";}
@if(isPhraseWord)
    !{phraseWord = "phrase-word";}
@endif

!{var familiarity = "unknown";}
@if(translation != null)
    !{familiarity = translation.familiarity().name().toLowerCase();}
@endif

!{var requestString = "/translations/update?selectedWordId=" + wordId + "&documentId=" + documentId + "&targetWord=" + processedWord;}
@if(translation == null)
    !{requestString = "/translations/add?selectedWordId=" + wordId + "&documentId=" + documentId + "&targetWord=" + processedWord;}
@endif

@if(isSelectable)
    <div
    class="
    inline-block relative cursor-pointer rounded whitespace-nowrap text-lg word selectable word-${wordId} value-${processedWord}"
    id="selectable-${wordId}"
    data-id="${wordId}"
    hx-get="${requestString}"
    hx-target="#translation-form-container-${wordId}"
    hx-swap="innerHTML"
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
        class="${familiarity} selectable-${processedWord} ${bgColor} ${textColor} rounded selectable"
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
        class="${familiarity} ${bgColor} ${textColor} rounded ${phraseWord}"
        >${word}</span>
    </div>
@endif
