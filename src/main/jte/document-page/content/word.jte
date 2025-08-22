@import lule.dictionary.enumeration.Familiarity

@param int wordId
@param int documentId
@param String word
@param String processedWord
@param String bgColor = ""
@param String textColor = ""
@param boolean isSelectable = true
@param boolean isPhraseWord = false
@param Familiarity familiarity = null
@param boolean isFound = false

!{var phraseWord = "";}
@if(isPhraseWord)
    !{phraseWord = "phrase-word";}
@endif

!{var familiarityString = "unknown";}
@if(familiarity != null)
    !{familiarityString = familiarity.name().toLowerCase();
}
@endif

<input
id="selectable-input-${wordId}"
type="hidden"
data-is-found="${isFound}"
class="selectable-state-${processedWord}">

@if(isSelectable)
    <div
    class="
    ${familiarityString} border border-2 rounded-none border-transparent inline-block relative cursor-pointer rounded whitespace-nowrap text-lg word selectable word-${wordId} word value-${processedWord}"
    id="selectable-${wordId}"
    data-id="${wordId}"
    hx-get="/translations/find"
    hx-target="#translation-form-container-${wordId}"
    hx-swap="innerHTML"
    hx-vals='js:{
        documentId: ${documentId},
        targetWord: "${processedWord}",
        selectedWordId: ${wordId},
        isPhrase: false,
        isFound: htmx.find("#selectable-input-${wordId}").dataset.isFound ?? false
    }'
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
        class="selectable-${processedWord} ${bgColor} ${textColor} rounded selectable"
        data-familiarity="${familiarityString}"
        id="word-span-${wordId}">${word}</span>
    </div>
@else
    <div
    class="
    inline-block relative rounded whitespace-nowrap text-lg pointer-events-none word-${wordId} word value-${processedWord}"
    id="word-${wordId}"
    data-id="${wordId}"
    hx-get="/translations/find"
    hx-target="#translation-form-container-${wordId}"
    hx-swap="innerHTML"
    hx-vals='js:{
        documentId: ${documentId},
        targetWord: "${processedWord}",
        selectedWordId: ${wordId},
        isPhrase: false,
        isFound: htmx.find("#selectable-input-${wordId}").dataset.isFound ?? false
    }'
    hx-trigger="click"
    hx-on::before-request="htmx.find('selectable-highlighter').updateSelectableId(${wordId})">
        <input type="hidden" name="isFound" value="${isFound}">
        <div
        onclick="event.stopPropagation()"
        class="
        cursor-default z-2 absolute inline left-2 top-10"
        id="translation-form-container-${wordId}"
        hx-on::after-swap="htmx.find('import-page').updatePositions(${wordId})">
        </div>
        <span
        class="${bgColor} ${textColor} rounded ${phraseWord}"
        >${word}</span>
    </div>
@endif
